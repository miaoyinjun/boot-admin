package org.jjche.log.biz.starter.support.aop;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.StaticLog;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jjche.common.api.CommonLogApi;
import org.jjche.common.constant.FilterEncConstant;
import org.jjche.common.constant.LogConstant;
import org.jjche.common.constant.SpringPropertyConstant;
import org.jjche.common.context.ContextUtil;
import org.jjche.log.context.LogRecordContext;
import org.jjche.common.dto.LogRecordDTO;
import org.jjche.common.abs.AbstractR;
import org.jjche.common.util.HttpUtil;
import org.jjche.common.util.StrUtil;
import org.jjche.common.util.ThrowableUtil;
import org.jjche.core.annotation.controller.ApiRestController;
import org.jjche.core.util.LogUtil;
import org.jjche.core.util.RequestHolder;
import org.jjche.core.util.SpringContextHolder;
import org.jjche.log.biz.beans.MethodExecuteResult;
import org.jjche.log.biz.service.IFunctionService;
import org.jjche.log.biz.service.ILogRecordService;
import org.jjche.log.biz.service.IOperatorGetService;
import org.jjche.log.biz.service.impl.DiffParseFunction;
import org.jjche.log.biz.starter.support.parse.LogFunctionParser;
import org.jjche.log.biz.starter.support.parse.LogRecordValueParser;
import org.slf4j.MDC;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 日志拦截器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
public class LogRecordInterceptor extends LogRecordValueParser
        implements MethodInterceptor, Serializable, SmartInitializingSingleton {

    ThreadLocal<Long> currentTime = new ThreadLocal<>();
    private LogRecordOperationSource logRecordOperationSource;
    private String tenantId;
    private ILogRecordService bizLogService;
    private IOperatorGetService operatorGetService;
    private CommonLogApi commonLogApi;
    private boolean joinTransaction;

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        return execute(invocation, invocation.getThis(), method, invocation.getArguments());
    }

    /**
     * <p>
     * 记录
     * </p>
     *
     * @param invoker 调用
     * @param target  目的
     * @param method  方法
     * @param args    参数
     * @return 结果
     */
    private Object execute(MethodInvocation invoker, Object target, Method method, Object[] args) throws Throwable {
        currentTime.set(System.currentTimeMillis());
        Class<?> targetClass = getTargetClass(target);
        Object ret = null;
        MethodExecuteResult methodExecuteResult = new MethodExecuteResult(method, args, targetClass);
        LogRecordContext.putEmptySpan();
        //appId单独处理不丢失
        String appId = RequestHolder.getHeader(FilterEncConstant.APP_ID);
        if (StrUtil.isNotBlank(appId)) {
            LogRecordContext.putVariable(FilterEncConstant.APP_ID, appId);
        }
        Collection<LogRecordDTO> logRecords = new ArrayList<>();
        Map<String, String> functionNameAndReturnMap = MapUtil.newHashMap();
        //获取方法执行前的模板
        try {
            logRecords = logRecordOperationSource.computeLogRecordOperations(method, targetClass);
            List<String> spElTemplates = getBeforeExecuteFunctionTemplate(logRecords);
            functionNameAndReturnMap = processBeforeExecuteFunctionTemplate(spElTemplates, targetClass, method, args);
        } catch (Exception e) {
            StaticLog.error("log record parse before function exception:{}", e);
        }

        try {
            //执行后
            ret = invoker.proceed();
            methodExecuteResult.setResult(ret);
            methodExecuteResult.setSuccess(true);
        } catch (Exception e) {
            methodExecuteResult.setSuccess(false);
            methodExecuteResult.setThrowable(e);
            methodExecuteResult.setErrorMsg(e.getMessage());
        }
        try {
            if (!CollectionUtils.isEmpty(logRecords)) {
                String methodName = targetClass.getName() + "." + method.getName() + "()";
                recordExecute(ret, methodName, targetClass, methodExecuteResult, functionNameAndReturnMap, logRecords);
            }
        } catch (Exception t) {
            //记录日志错误不要影响业务
            StaticLog.error("log record parse exception:{}", ThrowableUtil.getStackTrace(t));
        } finally {
            LogRecordContext.clear();
            //全局异常时不保存日志标记
            ContextUtil.setLogSaved(true);
        }
        Throwable throwable = methodExecuteResult.getThrowable();
        if (throwable != null) {
            throw throwable;
        }
        return ret;
    }

    /**
     * <p>
     * 获取在方法之前运行的模板
     * </p>
     *
     * @param logRecords /
     * @return /
     */
    private List<String> getBeforeExecuteFunctionTemplate(Collection<LogRecordDTO> logRecords) {
        List<String> spElTemplates = new ArrayList<>();
        for (LogRecordDTO operation : logRecords) {
            //执行之前的函数，失败模版不解析
            List<String> templates = getSpElTemplates(operation);
            if (!CollectionUtils.isEmpty(templates)) {
                spElTemplates.addAll(templates);
            }
        }
        return spElTemplates;
    }

    /**
     * <p>
     * 记录日志
     * </p>
     *
     * @param ret                      结果
     * @param methodName               方法
     * @param targetClass              目的类
     * @param methodExecuteResult      结果
     * @param functionNameAndReturnMap 方法名和返回
     */
    private void recordExecute(Object ret, String methodName, Class<?> targetClass, MethodExecuteResult methodExecuteResult, Map<String, String> functionNameAndReturnMap, Collection<LogRecordDTO> operations) {
        for (LogRecordDTO logRecord : operations) {
            try {
                //满足条件才记录
                if (exitsCondition(methodExecuteResult, functionNameAndReturnMap, logRecord)) {continue;}
                String errorMsg = methodExecuteResult.getErrorMsg();
                boolean success = methodExecuteResult.isSuccess();

                String reqId = MDC.get(LogConstant.REQUEST_ID);
                logRecord.setRequestId(reqId);
                Throwable throwable = methodExecuteResult.getThrowable();
                //异常数据
                if (throwable != null) {
                    logRecord.setExceptionDetail(ThrowableUtil.getStackTrace(throwable).getBytes());
                }
                //获取需要解析的表达式
                List<String> spElTemplates = getSpElTemplates(logRecord);
                String operatorIdFromService = getOperatorIdFromServiceAndPutTemplate(logRecord, spElTemplates);
                String bizNo = logRecord.getBizNo();
                String bizKey = logRecord.getBizKey();
                if (StrUtil.isBlank(bizKey)) {
                    String[] restPrefixArray = AnnotationUtil.getAnnotationValue(targetClass, ApiRestController.class);
                    if (ArrayUtil.isNotEmpty(restPrefixArray)) {
                        bizKey = ArrayUtil.get(restPrefixArray, 0);
                    }
                }
                String detail = logRecord.getDetail();
                String value = logRecord.getValue();
                String result = HttpUtil.UNKNOWN;
                if (success) {
                    if (ObjectUtil.isNotNull(ret)) {
                        //是否控制器返回类型
                        boolean isR = ClassUtil.isAssignable(AbstractR.class, ret.getClass());
                        if (isR) {
                            AbstractR iR = Convert.convert(AbstractR.class, ret);
                            result = iR.getMessage();
                        } else {
                            result = StrUtil.toString(ret);
                        }
                    }
                } else {
                    result = errorMsg;
                }

                Long time = System.currentTimeMillis() - currentTime.get();
                Map<String, String> expressionValues = processTemplate(spElTemplates, methodExecuteResult, functionNameAndReturnMap);

                String operatorId = getRealOperatorId(logRecord, operatorIdFromService, expressionValues);
                //获取请求客户端信息
                LogUtil.setLogRecordHttpRequest(logRecord);

                LogRecordDTO newLogRecord = setRecord(logRecord, bizKey, expressionValues.get(bizNo), operatorId, expressionValues.get(value), expressionValues.get(detail), success, methodName, result, time);
                commonLogApi.recordLog(newLogRecord);
            } catch (Exception t) {
                StaticLog.error("log record execute exception:{}", ThrowableUtil.getStackTrace(t));
                if (joinTransaction) {throw t;}
            } finally {
            }
        }
        currentTime.remove();
    }

    private boolean exitsCondition(MethodExecuteResult methodExecuteResult, Map<String, String> functionNameAndReturnMap, LogRecordDTO operation) {
        if (!StrUtil.isEmpty(operation.getCondition())) {
            String condition = singleProcessTemplate(methodExecuteResult, operation.getCondition(), functionNameAndReturnMap);
            if (StringUtils.endsWithIgnoreCase(condition, "false")) {return true;}
        }
        return false;
    }

    /**
     * <p>
     * 设置日志字段
     * </p>
     *
     * @param logRecord  /
     * @param bizKey     /
     * @param bizNo      /
     * @param operatorId /
     * @param value      /
     * @param detail     /
     * @param success    /
     * @param methodName /
     * @param result     /
     * @param time       /
     * @return /
     */
    private LogRecordDTO setRecord(LogRecordDTO logRecord, String bizKey, String bizNo, String operatorId, String value, String detail, boolean success, String methodName, String result, Long time) {
        LogRecordDTO newLogRecordDTO = ObjectUtil.clone(logRecord);
        newLogRecordDTO.setBizKey(bizKey);
        newLogRecordDTO.setBizNo(bizNo);
        newLogRecordDTO.setOperator(operatorId);
        newLogRecordDTO.setValue(value);
        newLogRecordDTO.setDetail(detail);
        newLogRecordDTO.setSuccess(success);
        newLogRecordDTO.setMethod(methodName);
        newLogRecordDTO.setTenant(tenantId);
        newLogRecordDTO.setResult(result);

        //save log 需要新开事务，失败日志不能因为事务回滚而丢失
        if (bizLogService == null) {
            StaticLog.error("bizLogService not init!!");
        }
        String appName = SpringContextHolder.getProperties(SpringPropertyConstant.APP_NAME);
        newLogRecordDTO.setAppName(appName);
        newLogRecordDTO.setTime(time);
        return newLogRecordDTO;
    }

    /**
     * <p>
     * 获取模板
     * </p>
     *
     * @param logRecord 日志
     * @return /
     */
    private List<String> getSpElTemplates(LogRecordDTO logRecord) {
        List<String> spElTemplates = CollUtil.newArrayList(logRecord.getBizKey(), logRecord.getBizNo(), logRecord.getValue(), logRecord.getDetail());
        if (StrUtil.isNotBlank(logRecord.getCondition())) {
            spElTemplates.add(logRecord.getCondition());
        }
        return spElTemplates;
    }

    private boolean logConditionPassed(String condition, Map<String, String> expressionValues) {
        return StrUtil.isBlank(condition) || StrUtil.endWithIgnoreCase(expressionValues.get(condition), "true");
    }

    private String getRealOperatorId(LogRecordDTO logRecord, String operatorIdFromService, Map<String, String> expressionValues) {
        return StrUtil.isNotBlank(operatorIdFromService) ? operatorIdFromService : expressionValues.get(logRecord.getOperatorId());
    }

    private String getOperatorIdFromServiceAndPutTemplate(LogRecordDTO operation, List<String> spElTemplates) {

        String realOperatorId = "";
        if (StrUtil.isBlank(operation.getOperatorId())) {
            realOperatorId = operatorGetService.getUser().getOperatorId();
            if (StrUtil.isBlank(realOperatorId)) {
                throw new IllegalArgumentException("[LogRecordDTO] operator is null");
            }
        } else {
            spElTemplates.add(operation.getOperatorId());
        }
        return realOperatorId;
    }

    /**
     * <p>
     * 获取目的类
     * </p>
     *
     * @param target 对象
     * @return 目的类
     */
    private Class<?> getTargetClass(Object target) {
        return AopProxyUtils.ultimateTargetClass(target);
    }

    /**
     * <p>
     * 获取操作人
     * </p>
     *
     * @return 操作人
     */
    public LogRecordOperationSource getLogRecordOperationSource() {
        return logRecordOperationSource;
    }

    /**
     * <p>
     * 设置操作人
     * </p>
     *
     * @param logRecordOperationSource 操作人
     */
    public void setLogRecordOperationSource(LogRecordOperationSource logRecordOperationSource) {
        this.logRecordOperationSource = logRecordOperationSource;
    }

    /**
     * <p>
     * 设置租户
     * </p>
     *
     * @param tenant 租户
     */
    public void setTenant(String tenant) {
        this.tenantId = tenant;
    }

    /**
     * <p>
     * 设置日志记录回调类
     * </p>
     *
     * @param bizLogService 回调类
     */
    public void setLogRecordService(ILogRecordService bizLogService) {
        this.bizLogService = bizLogService;
    }

    public void setBizLogService(ILogRecordService bizLogService) {
        this.bizLogService = bizLogService;
    }

    public void setCommonAPI(CommonLogApi commonLogApi) {
        this.commonLogApi = commonLogApi;
    }

    public void setJoinTransaction(boolean joinTransaction) {
        this.joinTransaction = joinTransaction;
    }

    @Override
    public void afterSingletonsInstantiated() {
        bizLogService = beanFactory.getBean(ILogRecordService.class);
        operatorGetService = beanFactory.getBean(IOperatorGetService.class);
        this.setLogFunctionParser(new LogFunctionParser(beanFactory.getBean(IFunctionService.class)));
        this.setDiffParseFunction(beanFactory.getBean(DiffParseFunction.class));
    }
}

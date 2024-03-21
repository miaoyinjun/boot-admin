package org.jjche.sys.modules.logging.aspect;//package com.boot.admin.log.modules.logging.aspect;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.convert.Convert;
//import cn.hutool.core.lang.Assert;
//import cn.hutool.core.map.MapUtil;
//import cn.hutool.core.util.*;
//import cn.hutool.extra.servlet.ServletUtil;
//import cn.hutool.extra.spring.SpringUtil;
//import cn.hutool.http.useragent.UserAgent;
//import cn.hutool.json.JSONObject;
//import cn.hutool.json.JSONUtil;
//import cn.hutool.log.StaticLog;
//import com.boot.admin.log.modules.logging.domain.LogDO;
//import com.boot.admin.common.dto.LogUpdateDetailDTO;
//import com.boot.admin.log.modules.logging.service.LogService;
//import com.boot.admin.common.util.ClassCompareUtil;
//import com.boot.admin.common.annotation.BizLog;
//import com.boot.admin.common.enums.LogType;
//import com.boot.admin.common.util.HttpUtil;
//import com.boot.admin.common.util.ThrowableUtil;
//import com.boot.admin.core.util.RequestHolder;
//import com.boot.admin.core.util.SecurityUtil;
//import io.lettuce.core.dynamic.support.ReflectionUtils;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.http.HttpHeaders;
//import org.springframework.web.util.ContentCachingRequestWrapper;
//
//import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Method;
//import java.util.*;
//
///**
// * <p>LogAspect class.</p>
// *
// * @author Zheng Jie
// * @version 1.0.8-SNAPSHOT
// * @since 2018-11-24
// */
//public class LogAspect {
//
//    private final LogService logService;
//
//    ThreadLocal<Long> currentTime = new ThreadLocal<>();
//
//    /**
//     * <p>Constructor for LogAspect.</p>
//     *
//     * @param logService a {@link LogService} object.
//     */
//    public LogAspect(LogService logService) {
//        this.logService = logService;
//    }
//
//    /**
//     * 配置切入点
//     */
//    @Pointcut("@annotation(com.boot.admin.common.annotation.BizLog)")
//    public void logPointcut() {
//        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
//    }
//
//    /**
//     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
//     *
//     * @param joinPoint join point for advice
//     * @return a {@link java.lang.Object} object.
//     * @throws java.lang.Throwable if any.
//     */
//    @Around("logPointcut()")
//    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//        Object result = null;
//        currentTime.set(System.currentTimeMillis());
//        LogDO log = new LogDO();
//        log = setLogRequest(log, joinPoint);
//        result = joinPoint.proceed();
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";
//        List<Object> argValues = new ArrayList<>(Arrays.asList(joinPoint.getArgs()));
//        log.setTime(System.currentTimeMillis() - currentTime.get());
////        logService.save(getUsername(signature.getName(), argValues), methodName, log);
//        currentTime.remove();
//        return result;
//    }
//
//    /**
//     * 配置异常通知
//     *
//     * @param joinPoint join point for advice
//     * @param e         exception
//     */
//    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
//    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
//        StackTraceElement[] stackTraceElements = e.getStackTrace();
//        boolean isSave = true;
//        //不保存cn.hutool.core.lang.Assert的验证异常抛出
//        if (ArrayUtil.isNotEmpty(stackTraceElements)) {
//            StackTraceElement stackTraceElement = stackTraceElements[0];
//            String stackTraceElementClassName = stackTraceElement.getClassName();
//            String assertionClassName = ClassUtil.getClassName(Assert.class, false);
//            isSave = !StrUtil.equals(stackTraceElementClassName, assertionClassName);
//        }
//        if (isSave) {
//            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//            // 方法路径
//            String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";
//            LogDO log = new LogDO();
//            log = setLogRequest(log, joinPoint);
////            log.setExceptionDetail(ThrowableUtil.getStackTrace(e).getBytes());
//            List<Object> argValues = new ArrayList<>(Arrays.asList(joinPoint.getArgs()));
//            log.setTime(System.currentTimeMillis() - currentTime.get());
////            logService.save(getUsername(signature.getName(), argValues), methodName, log);
//            currentTime.remove();
//        }
//    }
//
//    /**
//     * <p>
//     * 获取当前登录人用户名
//     * </p>
//     *
//     * @return 用户名
//     * @param path a {@link java.lang.String} object.
//     * @param argValues a {@link java.util.List} object.
//     */
//    public String getUsername(String path, List<Object> argValues) {
//        try {
//            return SecurityUtil.getCurrentUsername();
//        } catch (Exception e) {
//            String loginPath = "login";
//            if (loginPath.equals(path)) {
//                try {
//                    return new JSONObject(argValues.get(0)).get("username").toString();
//                } catch (Exception ea) {
//                    return "";
//                }
//            }
//            return "";
//        }
//    }
//
//    /**
//     * <p>
//     * 保存请求参数
//     * </p>
//     *
//     * @param log       日志
//     * @param joinPoint 方法
//     * @return 日志对象
//     */
//    private LogDO setLogRequest(LogDO log, JoinPoint joinPoint) {
//        try {
//            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//            BizLog ctlLog = signature.getMethod().getAnnotation(BizLog.class);
//            HttpServletRequest request = RequestHolder.getHttpServletRequest();
//            String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
//            UserAgent userAgent1 = HttpUtil.getUserAgent(request);
//            String os = ObjectUtil.isNull(userAgent1) ? HttpUtil.UNKNOWN : userAgent1.getOs().getName();
//            String browser = com.boot.admin.common.util.StrUtil.getBrowser(request);
//            String ip = com.boot.admin.common.util.StrUtil.getIp(request);
//            String parameterKey = ctlLog.parameterKey();
//            Class parameterKeyType = ctlLog.parameterKeyType();
//            LogType logActionType = ctlLog.type();
//
//            if (request instanceof ContentCachingRequestWrapper) {
//                ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
//                if (!ServletUtil.isMultipart(wrapper)) {
//                    String uri = wrapper.getRequestURI();
//                    Map<String, Object> paramMap = new HashMap<>(2);
//                    log.setModule(ctlLog.module());
//                    log.setUrl(uri);
////                    log.setAction(ctlLog.type().name());
//                    //是否保存参数
//                    if (ctlLog.isSaveParams()) {
//                        Map<String, String> paramsQueryMap = ServletUtil.getParamMap(wrapper);
////                    String paramsBodyStr = StrUtil.str(wrapper.getContentAsByteArray(), Charset.forName(wrapper.getCharacterEncoding()));
////                    JSONObject jsonObjectBody = JSONUtil.parseObj(paramsBodyStr);
//                        Object paramsBody = null;
//                        String detail = null;
//                        //参数值
//                        List<Object> argValues = new ArrayList<>(Arrays.asList(joinPoint.getArgs()));
//                        if (CollUtil.isNotEmpty(argValues)) {
//                            paramsBody = argValues.get(0);
//                        }
//                        paramMap.put("body", paramsBody);
//                        paramMap.put("query", paramsQueryMap);
//                        if (paramsBody != null) {
//                            String keyValStr = "";
//                            Object keyVal = null;
//                            //新增，删除，不能是数组
//                            Boolean isArrayParamsBody = Collection.class.isInstance(paramsBody);
//                            Boolean isGetParameterKey = (logActionType == LogType.ADD || logActionType == LogType.UPDATE) && !isArrayParamsBody;
//                            if (isGetParameterKey) {
//                                JSONObject paramsBodyJson = JSONUtil.parseObj(paramsBody);
//                                keyVal = Convert.convert(parameterKeyType, paramsBodyJson.get(parameterKey));
//                                keyValStr = Convert.toStr(keyVal);
////                                log.setParameterKey(keyValStr);
//                            }
////                            log.setParameterKeyDesc(ctlLog.parameterKeyDesc());
//                            //新增
//                            if (logActionType == LogType.ADD) {
//                                Map<String, String> paramsBodyDetailMap = CollUtil.newHashMap();
//                                Map<String, String> paramsBodyApiModelPropertyMap = ClassUtil.getApiModelPropertyValue(paramsBody);
//                                Map<String, String> paramsBodyMap = Convert.convert(Map.class, paramsBody);
//                                for (String key : paramsBodyApiModelPropertyMap.keySet()) {
//                                    String value = MapUtil.getStr(paramsBodyMap, key);
//                                    paramsBodyDetailMap.put(paramsBodyApiModelPropertyMap.get(key), value);
//                                }
//                                detail = JSONUtil.toJsonPrettyStr(paramsBodyDetailMap);
//                            }
//                            //是否保存修改前数据
//                            boolean isSaveBeforeData = ctlLog.isSaveBeforeData();
//                            if (BooleanUtil.isTrue(isSaveBeforeData)) {
//                                Class serviceClass = ctlLog.serviceClass();
//                                Assert.isFalse(BizLog.class.isInstance(serviceClass), "@BizLog请提供正确的参数serviceClass");
//                                String queryMethod = ctlLog.queryMethod();
//                                Method mh = ReflectionUtils.findMethod(SpringUtil.getBean(serviceClass).getClass(), queryMethod, parameterKeyType);
//                                Assert.isTrue(mh != null, "@BizLog请提供正确的参数queryMethod");
//                                StringBuilder detailSb = StrUtil.builder();
//                                //删除
//                                if (logActionType == LogType.DELETE) {
//                                    Object obj = ReflectionUtils.invokeMethod(mh, SpringUtil.getBean(serviceClass), paramsBody);
////                                    log.setBeforeData(JSONUtil.toJsonPrettyStr(obj));
//                                    String keyDesc = ctlLog.parameterKeyDesc();
//                                    detailSb.append("[");
//                                    detailSb.append(keyDesc);
//                                    detailSb.append("]：");
//                                    detailSb.append(paramsBody);
//                                    detail = StrUtil.removeSuffix(detailSb.toString(), "，");
//                                    keyValStr = Convert.toStr(paramsBody);
////                                    log.setParameterKey(keyValStr);
//                                }//修改
//                                else if (logActionType == LogType.UPDATE) {
//                                    //用spring bean获取操作前的参数,此处需要注意：传入的id类型与bean里面的参数类型需要保持一致
//                                    Assert.isTrue(StrUtil.isNotBlank(keyValStr), "@BizLog请提供正确的参数parameterKey");
//                                    Object obj = ReflectionUtils.invokeMethod(mh, SpringUtil.getBean(serviceClass), keyVal);
////                                    log.setBeforeData(JSONUtil.toJsonPrettyStr(obj));
//                                    //保存修改前后区别字段
//                                    List<LogUpdateDetailDTO> logUpdateDetailDTOList = ClassUtil.compareFieldsObject(obj, paramsBody);
//                                    if (CollUtil.isNotEmpty(logUpdateDetailDTOList)) {
//                                        for (LogUpdateDetailDTO updateDetailDTO : logUpdateDetailDTOList) {
//                                            detailSb.append("[");
//                                            detailSb.append("(");
//                                            detailSb.append(updateDetailDTO.getName());
//                                            detailSb.append(")");
//                                            detailSb.append("，");
//                                            detailSb.append("旧值：");
//                                            detailSb.append("'");
//                                            detailSb.append(updateDetailDTO.getOldVal());
//                                            detailSb.append("'");
//                                            detailSb.append("，");
//                                            detailSb.append("新值：");
//                                            detailSb.append("'");
//                                            detailSb.append(updateDetailDTO.getNewVal());
//                                            detailSb.append("'");
//                                            detailSb.append("]，");
//                                        }
//                                    }
//                                    detail = StrUtil.removeSuffix(detailSb.toString(), "，");
//                                }
//                            }
//                            log.setDetail(detail);
//                        }
//                        String paramStr = JSONUtil.toJsonPrettyStr(paramMap);
//                        log.setParams(paramStr);
//                    }
//                }
//            }
//            log.setDescription(ctlLog.value());
//            log.setRequestIp(ip);
//            log.setBrowser(browser);
//            log.setOs(os);
//            log.setUserAgent(userAgent);
//        } catch (Exception e) {
//            StaticLog.error(ThrowableUtil.getStackTrace(e));
//        }
//        return log;
//    }
//}

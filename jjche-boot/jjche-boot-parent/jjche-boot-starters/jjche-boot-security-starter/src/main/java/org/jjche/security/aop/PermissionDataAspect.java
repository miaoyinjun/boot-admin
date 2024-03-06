package org.jjche.security.aop;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.log.StaticLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jjche.common.annotation.PermissionData;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.api.CommonDataPermissionFieldApi;
import org.jjche.security.context.ElPermissionContext;
import org.jjche.common.base.BaseQueryCriteriaDTO;
import org.jjche.common.dto.PermissionDataResourceDTO;
import org.jjche.common.dto.PermissionDataRuleDTO;
import org.jjche.common.permission.DataPermissionFieldFilterable;
import org.jjche.common.permission.DataPermissionFieldMetaSetter;
import org.jjche.common.vo.DataScopeVO;
import org.jjche.common.util.ClassUtil;
import org.jjche.common.util.StrUtil;
import org.jjche.common.vo.DataPermissionFieldResultVO;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.util.SecurityUtil;
import org.jjche.security.permission.field.DataPermissionFieldResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据权限切面处理类
 * 当被请求的方法有注解PermissionData时,会在往当前request中写入数据权限信息
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-11-29
 */
@Aspect
@Component
public class PermissionDataAspect {

    @Autowired
    private CommonDataPermissionFieldApi commonDataPermissionFieldApi;

    /**
     * <p>permissionDataCut.</p>
     */
    @Pointcut("@annotation(org.jjche.common.annotation.PermissionData)")
    public void permissionDataCut() {

    }

    /**
     * <p>around.</p>
     *
     * @param point a {@link org.aspectj.lang.ProceedingJoinPoint} object.
     * @return a {@link java.lang.Object} object.
     * @throws java.lang.Throwable if any.
     */
    @Around("permissionDataCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object object = null;
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        PermissionData pd = method.getAnnotation(PermissionData.class);

        String permissionCode = ElPermissionContext.get();
        DataScopeVO dataScopeVO = null;
        //未登录情况下
        try {
            dataScopeVO = SecurityUtil.getUserDataScope();
        } catch (Exception e) {
        }
        BaseQueryCriteriaDTO paramQueryCriteriaDTO = null;
        boolean isNotAdmin = dataScopeVO != null && !dataScopeVO.isAll() && StrUtil.isNotBlank(permissionCode);

        //传输对象
        PermissionDataResourceDTO dto = new PermissionDataResourceDTO();
        dto.setPermission(permissionCode);
        dto.setFilter(isNotAdmin);

        //非管理员
        if (isNotAdmin) {
            permissionCode = StrUtil.sub(permissionCode, 0, StrUtil.indexOf(permissionCode, ':') + 1);
            permissionCode = StrUtil.appendIfMissing(permissionCode, "list");
            //行级
            List<PermissionDataRuleDTO> permissionDataRuleList = CollUtil.newArrayList();
            //dataScope定义
            String deptName = pd.deptIdInFieldName(),
                    creatorName = pd.userIdEQFieldName(),
                    userName = dataScopeVO.getUserName();
            Set<Long> deptIds = dataScopeVO.getDeptIds();
            boolean isSelf = dataScopeVO.isSelf();
            //部门id
            if (StrUtil.isNotBlank(deptName) && CollectionUtil.isNotEmpty(deptIds)) {
                PermissionDataRuleDTO permissionDataRuleDTO = PermissionDataRuleDTO.builder()
                        .column(deptName)
                        .condition(QueryCriteria.Type.IN)
                        .value(deptIds)
                        .build();
                permissionDataRuleList.add(permissionDataRuleDTO);
            }//作者
            else if (isSelf && StrUtil.isNotEmpty(creatorName)) {
                PermissionDataRuleDTO permissionDataRuleDTO = PermissionDataRuleDTO.builder()
                        .column(creatorName)
                        .condition(QueryCriteria.Type.EQUAL)
                        .value(userName)
                        .build();
                permissionDataRuleList.add(permissionDataRuleDTO);
            }

            //获取用户数据规则配置
            List<PermissionDataRuleDTO> permissionDataRuleDTOList =
                    commonDataPermissionFieldApi.listPermissionDataRuleByUserId(SecurityUtil.getUserId());
            if (CollUtil.isNotEmpty(permissionDataRuleDTOList)) {
                String finalPermissionCode = permissionCode;
                Predicate condition = (str) -> StrUtil.equals(String.valueOf(str), finalPermissionCode);
                permissionDataRuleDTOList = permissionDataRuleDTOList.stream().filter((p) -> (condition.test(p.getMenuPermission()))).collect(Collectors.toList());
                // TODO: 2021/11/17  系统上下文变量
//                    if (CollUtil.isNotEmpty(permissionDataRuleDTOList)) {
//                        for (PermissionDataRuleDTO ruleDTO : permissionDataRuleDTOList) {
//                            Object value = ruleDTO.getValue();
//                            if (value instanceof String) {
//                                value = StrUtil.replace(String.valueOf(value), "", "");
//                                ruleDTO.setValue(value);
//                            }
//                        }
//                    }
                permissionDataRuleList.addAll(permissionDataRuleDTOList);
            }

            Object[] args = point.getArgs();
            if (ArrayUtil.isNotEmpty(args)) {
                for (Object arg : args) {
                    if (arg instanceof BaseQueryCriteriaDTO) {
                        paramQueryCriteriaDTO = (BaseQueryCriteriaDTO) arg;
                        paramQueryCriteriaDTO.setPermissionDataRuleList(permissionDataRuleList);
                        break;
                    }
                }
            }
        }

        //获取泛型
        // 1层，如DataPermissionFieldResult<StudentVO>
        // 2层，如R<MyPage<StudentVO>>
        Type returnType = this.getTypeArgumentVO(method.getGenericReturnType());
        Class returnClass = TypeUtil.getClass(returnType);
        //场景：修改，单个对象列级，入参
        //如果字段没有修改权限，将字段置null，mybatis不会处理null值字段
        if (pd.fieldUpdate() && paramQueryCriteriaDTO != null) {
            returnClass = paramQueryCriteriaDTO.getClass();
            Map<String, String> voMap = ClassUtil.getApiModelPropertyValue(returnClass);
            dto.setVoMap(voMap);
            this.doFilter(DataPermissionFieldResult.build(paramQueryCriteriaDTO), dto, true);
        }
        object = point.proceed();
        //列级
        //必须有返回值
        if (pd.fieldReturn() && ObjectUtil.isNotEmpty(object)) {
            if (object != null) {
                Object newObject = null;
                boolean isR = ClassUtil.isAssignable(R.class, object.getClass());
                boolean isDataPermissionFieldResult = ClassUtil.isAssignable(DataPermissionFieldResult.class, object.getClass());
                //控制器
                if (isR) {
                    R R = (R) object;
                    newObject = R.getData();
                } else if (isDataPermissionFieldResult) {
                    newObject = object;
                }
                if (newObject != null) {
                    List<DataPermissionFieldResultVO> resources = null;
                    //行
                    if (newObject instanceof DataPermissionFieldFilterable) {
                        Map<String, String> voMap = ClassUtil.getApiModelPropertyValue(returnClass);
                        dto.setVoMap(voMap);
                        resources = this.doFilter((DataPermissionFieldFilterable) newObject, dto, false);
                    }
                    //列
                    if (newObject instanceof DataPermissionFieldMetaSetter) {
                        ((DataPermissionFieldMetaSetter) newObject).setMeta(resources);
                    }
                }
            }
        }
        if (object == null) {
            object = point.proceed();
        }
        return object;
    }

    /**
     * 执行过滤操作
     *
     * @param filterable 方法返回的对象
     * @param dto        /
     * @param editable   是否可编辑
     * @return 查询内容
     */
    private List<DataPermissionFieldResultVO> doFilter(DataPermissionFieldFilterable<?> filterable,
                                                       PermissionDataResourceDTO dto,
                                                       boolean editable) {
        List<DataPermissionFieldResultVO> resources = this.commonDataPermissionFieldApi.listPermissionDataResource(dto);
        if (CollUtil.isEmpty(resources)) {
            return null;
        }

        Map<String, DataPermissionFieldResultVO> dataColumnMap = new HashMap<>(resources.size());
        for (DataPermissionFieldResultVO column : resources) {
            dataColumnMap.put(column.getCode(), column);
        }
        filterable.doFilter(o -> {
            PropertyDescriptor[] propertyDescriptors = BeanUtil.getPropertyDescriptors(o.getClass());
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String name = propertyDescriptor.getName();
                DataPermissionFieldResultVO dataColumn = dataColumnMap.get(name);
                boolean isAccessibleOrEditable = dataColumn != null && (!dataColumn.getIsAccessible() || (editable && !dataColumn.getIsEditable()));
                if (isAccessibleOrEditable) {
                    try {
                        propertyDescriptor.getWriteMethod().invoke(o, new Object[]{null});
                    } catch (Exception ex) {
                        // skip
                        StaticLog.error("字段置null失败");
                    }
                }
            }
            return o;
        });
        return resources;
    }

    /**
     * 获取此方法对应的所有数据资源项
     *
     * @param methodName 拦截的方法名称
     * @return 用户的数据权限
     */
    private List<DataPermissionFieldResultVO> findAuthorityDataResource(String methodName) {
        return null;
    }

    /**
     * <p>
     * 获取方法名
     * </p>
     *
     * @param joinPoint aop
     * @return 方法名
     * @author miaoyj
     * @since 2020-12-09
     */
    private String getMethodName(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;

            StringBuilder sb = new StringBuilder();

            sb.append(methodSignature.getDeclaringTypeName());
            sb.append(".");
            sb.append(methodSignature.getName());
            sb.append("(");
            Class<?>[] parametersTypes = methodSignature.getParameterTypes();
            for (int i = 0; i < parametersTypes.length; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                Class<?> parametersType = parametersTypes[i];
                sb.append(parametersType.getSimpleName());
            }
            sb.append(")");
            return sb.toString();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(signature.getDeclaringTypeName());
            sb.append(".");
            sb.append(signature.getName());
            return sb.toString();
        }
    }

    /**
     * <p>
     * 获取VO结尾的class泛型
     * </p>
     *
     * @param type 返回类型
     * @return /
     */
    private Type getTypeArgumentVO(Type type) {
        Type resultType = TypeUtil.getTypeArgument(type);
        if (resultType != null) {
            String className = resultType.getTypeName();
            if (!StrUtil.endWith(className, "VO")) {
                return getTypeArgumentVO(resultType);
            }
        }
        return resultType;
    }

}

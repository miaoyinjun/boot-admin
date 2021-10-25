package com.boot.admin.mybatis.aop;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.boot.admin.common.annotation.PermissionData;
import com.boot.admin.common.annotation.QueryCriteria;
import com.boot.admin.common.dto.BaseQueryCriteriaDTO;
import com.boot.admin.common.dto.PermissionDataRuleDTO;
import com.boot.admin.common.pojo.DataScope;
import com.boot.admin.core.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * 数据权限切面处理类
 * 当被请求的方法有注解PermissionData时,会在往当前request中写入数据权限信息
 *
 * @Date 2019年4月10日
 * @Version: 1.0
 */
@Aspect
@Component
@Slf4j
public class PermissionDataAspect {
//
//    @Autowired
//    private CommonAPI commonAPI;

    @Pointcut("@annotation(com.boot.admin.common.annotation.PermissionData)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        PermissionData pd = method.getAnnotation(PermissionData.class);
        String component = pd.pageComponent();
        List<PermissionDataRuleDTO> permissionDataRuleList = CollUtil.newArrayList();

        DataScope dataScope = null;
        //未登录情况下
        try {
            dataScope = SecurityUtils.getCurrentUserDataScope();
        } catch (Exception e) {
        }
        if (dataScope != null && !dataScope.isAll()) {
            //dataScope定义
            String deptName = pd.deptIdInFieldName(),
                    creatorName = pd.userIdEQFieldName(),
                    userName = dataScope.getUserName();
            Set<Long> deptIds = dataScope.getDeptIds();
            boolean isSelf = dataScope.isSelf();
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


//            PermissionDataRuleDTO permissionDataRuleDTO = PermissionDataRuleDTO.builder()
//                    .column("age")
//                    .condition(QueryCriteria.Type.SQL_RULES)
//                    .value("age  = 3")
//                    .build();
//            permissionDataRuleList.add(permissionDataRuleDTO);

            Object[] args = point.getArgs();
            if (ArrayUtil.isNotEmpty(args)) {
                for (Object arg : args) {
                    if (arg instanceof BaseQueryCriteriaDTO) {
                        BaseQueryCriteriaDTO queryCriteriaDTO = (BaseQueryCriteriaDTO) arg;
                        queryCriteriaDTO.setPermissionDataRuleList(permissionDataRuleList);
                    }
                }
            }
        }

        return point.proceed();
    }


}

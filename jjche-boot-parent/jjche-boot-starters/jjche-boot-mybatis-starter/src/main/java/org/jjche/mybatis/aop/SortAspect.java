//package com.boot.admin.mybatis.aop;
//
//import cn.hutool.core.util.EnumUtil;
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.StrUtil;
//import com.boot.admin.common.constant.PackageConstant;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//import tk.mybatis.orderbyhelper.OrderByHelper;
//
//import java.util.Map;
//
///**
// * <p>
// * 排序切面
// * </p>
// *
// * @author miaoyj
// * @version 1.0.8-SNAPSHOT
// * @since 2020-10-13
// */
//@Aspect
//@Component
//public class SortAspect {
//
//    /**
//     * <p>sortAspect.</p>
//     */
//    @Pointcut(value = "execution(public * " + PackageConstant.MAPPER_PATH_STAR + "..*(..)))")
//    public void sortAspect() {
//
//    }
//
//    /**
//     * <p>
//     * 设置分页排序
//     * </p>
//     *
//     * @param joinPoint a {@link org.aspectj.lang.JoinPoint} object.
//     * @author miaoyj
//     * @since 2020-10-13
//     */
//    @Before("sortAspect()")
//    public void doBefore(JoinPoint joinPoint) {
//        //获取传入目标方法的参数
//        Object[] args = joinPoint.getArgs();
//        Enum sortEnum = null;
//        for (int i = 0; i < args.length; i++) {
//            Object object = args[i];
//            if (ObjectUtil.isNotNull(object)) {
//                boolean isOrder = isOrder(object.toString());
//                if (Enum.class.isInstance(object) && isOrder) {
//                    sortEnum = (Enum) object;
//                    break;
//                }
//            }
//        }
//        if (sortEnum != null) {
//            Class<? extends Enum<?>> clazz = (Class<? extends Enum<?>>) sortEnum.getClass();
//            Map<String, Object> enumMap = EnumUtil.getNameFieldMap(clazz, "value");
//            String orderColumnName = enumMap.get(sortEnum.name()).toString();
//            OrderByHelper.orderBy(orderColumnName);
//        }
//    }
//
//    /**
//     * <p>
//     * 是否包含排序字段
//     * </p>
//     *
//     * @param orderColumnName 排序枚举
//     * @return 是否排序
//     * @author miaoyj
//     * @since 2020-10-30
//     */
//    private boolean isOrder(String orderColumnName) {
//        return StrUtil.endWithIgnoreCase(orderColumnName, "ASC")
//                || StrUtil.endWithIgnoreCase(orderColumnName, "DESC");
//    }
//}

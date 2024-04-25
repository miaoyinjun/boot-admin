//package com.boot.admin.mybatis.interceptor;
//
//import cn.hutool.core.map.MapUtil;
//import cn.hutool.core.util.EnumUtil;
//import cn.hutool.core.util.StrUtil;
//import org.apache.ibatis.binding.MapperMethod;
//import org.apache.ibatis.cache.CacheKey;
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.plugin.*;
//import org.apache.ibatis.session.ResultHandler;
//import org.apache.ibatis.session.RowBounds;
//
//import java.lang.reflect.Field;
//import java.util.Map;
//import java.util.Properties;
//
///**
// * <p>
// * mybatis拦截器，处理增改数据时设置创建修改人
// * </p>
// *
// * @author miaoyj
// * @version 1.0.0-SNAPSHOT
// * @since 2020-07-14
// */
//@Intercepts({
//        @Signature(type = Executor.class,
//                method = "update",
//                args = {MappedStatement.class, Object.class}),
//        @Signature(type = Executor.class,
//                method = "query",
//                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class,
//                        CacheKey.class, BoundSql.class}),
//})
//public class MyInterceptor implements Interceptor {
//    private Properties properties;
//
//    private static final String COMMAND_NAME_INSERT = "INSERT";
//    private static final String COMMAND_NAME_UPDATE = "UPDATE";
//    private static final String COMMAND_NAME_SELECT = "SELECT";
//
//    /** {@inheritDoc} */
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        Object[] args = invocation.getArgs();
//        //获取原始的ms
//        MappedStatement ms = (MappedStatement) args[0];
//        String commandName = ms.getSqlCommandType().name();
//        Object parameter = args[1];
//
//        BoundSql boundSql = ms.getBoundSql(parameter);
//        //获取到原始sql语句
//        String sql = boundSql.getSql();
//        String mSql = sql;
//        //通过反射修改sql语句
//        Field field = boundSql.getClass().getDeclaredField("sql");
//        field.setAccessible(true);
//        field.set(boundSql, mSql);
//
//        Map<String, Object> paramNewMap = MapUtil.newHashMap();
//        MapperMethod.ParamMap<Object> paramMap = (MapperMethod.ParamMap) parameter;
//        for (String key : paramMap.keySet()) {
//            Object value = paramMap.get(key);
//            if (Enum.class.isInstance(value)) {
//                paramNewMap.put(key, value);
//            }
//        }
//        if (MapUtil.isNotEmpty(paramNewMap)) {
//            for (String key : paramNewMap.keySet()) {
//                Object value = paramMap.get(key);
//                Enum sortEnum = (Enum) value;
//                Class<? extends Enum<?>> clazz = (Class<? extends Enum<?>>) sortEnum.getClass();
//                Map<String, Object> enumMap = EnumUtil.getNameFieldMap(clazz, "value");
//                String orderColumnName = enumMap.get(sortEnum.name()).toString();
//                if (StrUtil.endWithIgnoreCase(orderColumnName, " ASC")
//                        || StrUtil.endWithIgnoreCase(orderColumnName, " DESC")) {
//                    paramMap.put(key, orderColumnName);
//                }
//            }
//        }
//        args[1] = parameter;
//        return invocation.proceed();
//    }
//
//    /** {@inheritDoc} */
//    @Override
//    public Object plugin(Object target) {
//        return Plugin.wrap(target, this);
//    }
//
//    /** {@inheritDoc} */
//    @Override
//    public void setProperties(Properties properties) {
//        this.properties = properties;
//    }
//
//}

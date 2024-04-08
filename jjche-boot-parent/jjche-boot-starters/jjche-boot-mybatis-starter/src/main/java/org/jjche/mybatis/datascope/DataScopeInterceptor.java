///*
// *  Copyright (c) 2019-2020, somewhere (somewhere0813@gmail.com).
// *  <p>
// *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
// *  you may not use this file except in compliance with the License.
// *  You may obtain a copy of the License at
// *  <p>
// * https://www.gnu.org/licenses/lgpl.html
// *  <p>
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.boot.admin.mybatis.datascope;
//
//import cn.hutool.core.annotation.AnnotationUtil;
//import cn.hutool.core.collection.CollectionUtil;
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.StrUtil;
//import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
//import com.boot.admin.common.annotation.DataPermission;
//import com.boot.admin.common.pojo.DataScope;
//import com.boot.admin.core.util.SecurityUtil;
//import lombok.SneakyThrows;
//import net.sf.jsqlparser.expression.Alias;
//import net.sf.jsqlparser.expression.Expression;
//import net.sf.jsqlparser.expression.StringValue;
//import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
//import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
//import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
//import net.sf.jsqlparser.expression.operators.relational.InExpression;
//import net.sf.jsqlparser.expression.operators.relational.ItemsList;
//import net.sf.jsqlparser.parser.CCJSqlParserUtil;
//import net.sf.jsqlparser.schema.Column;
//import net.sf.jsqlparser.statement.Statement;
//import net.sf.jsqlparser.statement.delete.Delete;
//import net.sf.jsqlparser.statement.select.PlainSelect;
//import net.sf.jsqlparser.statement.select.Select;
//import net.sf.jsqlparser.statement.update.Update;
//import org.apache.ibatis.executor.statement.StatementHandler;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.mapping.SqlCommandType;
//import org.apache.ibatis.mapping.StatementType;
//import org.apache.ibatis.plugin.*;
//import org.apache.ibatis.reflection.MetaObject;
//import org.apache.ibatis.reflection.SystemMetaObject;
//
//import java.lang.reflect.Method;
//import java.sql.Connection;
//import java.util.Map;
//import java.util.Properties;
//import java.util.Set;
//import java.util.stream.Collectors;
///**
// * <p>
// * 数据权限拦截器
// * </p>
// *
// * @author miaoyj
// * @version 1.0.10-SNAPSHOT
// * @since 2020-11-05
// */
//@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
//public class DataScopeInterceptor implements Interceptor {
//
//    /** {@inheritDoc} */
//    @Override
//    @SneakyThrows
//    public Object intercept(Invocation invocation) {
//        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
//        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
////        this.sqlParser(metaObject);
//        // 先判断是不是SELECT操作  (2019-04-10 00:37:31 跳过存储过程)
//        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
//        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
//        boolean isDelete = SqlCommandType.DELETE == sqlCommandType;
//        boolean isUpdate = SqlCommandType.UPDATE == sqlCommandType;
//        boolean isSelect = SqlCommandType.SELECT == sqlCommandType;
//        boolean isNotCrud = !isDelete && !isUpdate && !isSelect;
//        if (isNotCrud || StatementType.CALLABLE == mappedStatement.getStatementType()) {
//            return invocation.proceed();
//        }
//
//        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
//        //查找参数中包含DataScope类型的参数
//        DataPermission dataScopePermission = getPermissionByDelegate(mappedStatement);
//        if (dataScopePermission == null) {
//            return invocation.proceed();
//        }
//        DataScope dataScope = null;
//        //未登录情况下
//        try {
//            dataScope = SecurityUtil.getCurrentUserDataScope();
//        } catch (Exception e) {
//        }
//        if (dataScope == null || dataScope.isAll()) {
//            return invocation.proceed();
//        } else {
//            String sql = null;
//            String originalSql = boundSql.getSql();
//            Statement statement = CCJSqlParserUtil.parse(originalSql);
//            Select selectStatement = null;
//            Delete deleteStatement = null;
//            Update updateStatement = null;
//            if (isSelect) {
//                selectStatement = (Select) statement;
//            } else if (isDelete) {
//                deleteStatement = (Delete) statement;
//            } else if (isUpdate) {
//                updateStatement = (Update) statement;
//            }
//            Expression whereExpression = null;
//            String aliaName = "";
//            //SELECT
//            if (ObjectUtil.isNotNull(selectStatement) && selectStatement.getSelectBody() instanceof PlainSelect) {
//                PlainSelect plainSelect = (PlainSelect) selectStatement.getSelectBody();
//                Alias alias = plainSelect.getFromItem().getAlias();
//                if (alias != null && StrUtil.isNotEmpty(alias.getName())) {
//                    aliaName = alias.getName() + StrUtil.DOT;
//                }
//                whereExpression = getExpression(dataScopePermission, dataScope, aliaName, plainSelect.getWhere());
//                plainSelect.setWhere(whereExpression);
//                sql = plainSelect.toString();
//                //DELETE
//            } else if (ObjectUtil.isNotNull(deleteStatement)) {
//                whereExpression = getExpression(dataScopePermission, dataScope, aliaName, deleteStatement.getWhere());
//                deleteStatement.setWhere(whereExpression);
//                sql = deleteStatement.toString();
//            }
//            //UPDATE
//            else if (ObjectUtil.isNotNull(updateStatement)) {
//                whereExpression = getExpression(dataScopePermission, dataScope, aliaName, updateStatement.getWhere());
//                updateStatement.setWhere(whereExpression);
//                sql = updateStatement.toString();
//            }
//            if (whereExpression != null) {
//                metaObject.setValue("delegate.boundSql.sql", sql);
//            }
//            return invocation.proceed();
//        }
//    }
//
//    /**
//     * {@inheritDoc}
//     * <p>
//     * 生成拦截对象的代理
//     */
//    @Override
//    public Object plugin(Object target) {
//        if (target instanceof StatementHandler) {
//            return Plugin.wrap(target, this);
//        }
//        return target;
//    }
//
//    /**
//     * {@inheritDoc}
//     * <p>
//     * mybatis配置的属性
//     */
//    @Override
//    public void setProperties(Properties properties) {
//
//    }
//
//    /**
//     * 查找参数是否包括DataScope对象
//     *
//     * @param parameterObj 参数列表
//     * @return DataScope
//     */
//    private DataScope findDataScopeObject(Object parameterObj) {
//        if (parameterObj instanceof DataScope) {
//            return (DataScope) parameterObj;
//        } else if (parameterObj instanceof Map) {
//            for (Object val : ((Map<?, ?>) parameterObj).values()) {
//                if (val instanceof DataScope) {
//                    return (DataScope) val;
//                }
//            }
//        }
//        return null;
//    }
//
//    /**
//     * <p>
//     * 获取权限注解
//     * </p>
//     *
//     * @param dataScopePermission 注解
//     * @param dataScope           数据权限
//     * @param aliaName            表别名
//     * @return
//     * @author miaoyj
//     * @since 2020-11-10
//     */
//    private Expression getExpression(DataPermission dataScopePermission, DataScope dataScope,
//                                     String aliaName, Expression whereExpression) {
//        Expression expression = null;
//        //dataScope定义
//        String scopeName = dataScopePermission.deptIdInFieldName(),
//                creatorName = dataScopePermission.userIdEQFieldName(),
////                userId = String.valueOf(dataScope.getUserId()),
//                userName = dataScope.getUserName();
//        Set<Long> deptIds = dataScope.getDeptIds();
//        boolean isSelf = dataScope.isSelf();
//        //机构id
//        if (StrUtil.isNotBlank(scopeName) && CollectionUtil.isNotEmpty(deptIds)) {
//            ItemsList itemsList = new ExpressionList(deptIds.stream().map(deptId -> new StringValue(String.valueOf(deptId))).collect(Collectors.toList()));
//            expression = new InExpression(new Column(aliaName + scopeName), itemsList);
//        }//作者
//        else if (isSelf && StrUtil.isNotEmpty(creatorName)) {
//            EqualsTo equalsTo = new EqualsTo();
//            equalsTo.setLeftExpression(new Column(aliaName + creatorName));
//            equalsTo.setRightExpression(new StringValue(userName));
//            expression = equalsTo;
//        }
//        if (whereExpression != null) {
//            whereExpression = new AndExpression(whereExpression, expression);
//        } else {
//            whereExpression = expression;
//        }
//        return whereExpression;
//    }
//
//    /**
//     * <p>
//     * 根据StatementHandler获取注解对象
//     * </p>
//     *
//     * @param mappedStatement
//     * @return
//     * @author miaoyj
//     * @since 2020-11-10
//     */
//    private static DataPermission getPermissionByDelegate(MappedStatement mappedStatement) {
//        DataPermission dataScopePermission = null;
//        try {
//            String id = mappedStatement.getId();
//            String className = id.substring(0, id.lastIndexOf("."));
//            String methodName = id.substring(id.lastIndexOf(".") + 1, id.length());
//            final Class cls = Class.forName(className);
//            //先查找类注解
//            dataScopePermission = AnnotationUtil.getAnnotation(cls, DataPermission.class);
//            if (dataScopePermission == null) {
//                //查找方法注解
//                final Method[] method = cls.getMethods();
//                for (Method me : method) {
//                    if (me.getName().equals(methodName) && me.isAnnotationPresent(DataPermission.class)) {
//                        dataScopePermission = me.getAnnotation(DataPermission.class);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return dataScopePermission;
//    }
//
//}

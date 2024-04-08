//package com.boot.mybatis.datascope;
//
//import cn.hutool.log.StaticLog;
//import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
//import com.boot.admin.common.pojo.DataScope;
//import com.boot.admin.common.util.StrUtil;
//import com.boot.admin.core.util.SecurityUtil;
//import lombok.SneakyThrows;
//import net.sf.jsqlparser.expression.Expression;
//import net.sf.jsqlparser.expression.StringValue;
//import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
//import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
//import net.sf.jsqlparser.expression.operators.relational.InExpression;
//import net.sf.jsqlparser.expression.operators.relational.ItemsList;
//import net.sf.jsqlparser.schema.Column;
//
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//public class MyDataPermissionHandler implements DataPermissionHandler {
//
//    /**
//     * @param where             原SQL Where 条件表达式
//     * @param mappedStatementId Mapper接口方法ID
//     * @return
//     */
//    @SneakyThrows
//    @Override
//    public Expression getSqlSegment(Expression where, String mappedStatementId) {
//        StaticLog.info("=========================== start MyDataPermissionHandler");
//        DataScope dataScope = null;
//        //未登录情况下
//        try {
//            dataScope = SecurityUtil.getCurrentUserDataScope();
//        } catch (Exception e) {
//        }
//        if (dataScope == null || dataScope.isAll()) {
//            return where;
//        }
//        String scopeName = "id";
//        String testId = "com.boot.admin.system.modules.system.mapper.DeptMapper.selectList";
//        Set<Long> deptIds = dataScope.getDeptIds();
//        List<Expression> expressions = deptIds.stream().map(deptId -> new StringValue(String.valueOf(deptId))).collect(Collectors.toList());
//        if (StrUtil.equals(mappedStatementId, testId)) {
//            ItemsList itemsList = new ExpressionList(expressions);
//            InExpression inExpression = new InExpression(new Column(scopeName), itemsList);
//            where = new AndExpression(where, inExpression);
//        }
//        return where;
//        // 1. 模拟获取登录用户，从用户信息中获取部门ID
////        Random random = new Random();
////        int userDeptId = random.nextInt(9) + 1; // 随机部门ID 1-10 随机数
////        Expression expression = null;
////        log.info("=============== userDeptId:{}", userDeptId);
////        if (userDeptId == DeptEnum.BOOS.getType()) {
////            // 2.userDeptId为1，说明是老总，可查看所有数据无需处理
////            return where;
////
////        } else if (userDeptId == DeptEnum.MANAGER_02.getType()) {
////            // 3. userDeptId为2，说明是02部门经理，可查看02部门及下属部门所有数据
////            // 创建IN 表达式
////            Set<String> deptIds = Sets.newLinkedHashSet(); // 创建IN范围的元素集合
////            deptIds.add("2");
////            deptIds.add("3");
////            deptIds.add("4");
////            deptIds.add("5");
////            ItemsList itemsList = new ExpressionList(deptIds.stream().map(StringValue::new).collect(Collectors.toList())); // 把集合转变为JSQLParser需要的元素列表
////            InExpression inExpression = new InExpression(new Column("order_tbl.dept_id"), itemsList); //  order_tbl.dept_id IN ('2', '3', '4', '5')
////            return new AndExpression(where, inExpression);
////        } else if (userDeptId == DeptEnum.MANAGER_06.getType()) {
////            // 4. userDeptId为6，说明是06部门经理，可查看06部门及下属部门所有数据
////            // 创建IN 表达式
////            Set<String> deptIds = Sets.newLinkedHashSet(); // 创建IN范围的元素集合
////            deptIds.add("6");
////            deptIds.add("7");
////            deptIds.add("8");
////            deptIds.add("9");
////            ItemsList itemsList = new ExpressionList(deptIds.stream().map(StringValue::new).collect(Collectors.toList())); // 把集合转变为JSQLParser需要的元素列表
////            InExpression inExpression = new InExpression(new Column("order_tbl.dept_id"), itemsList);
////            return new AndExpression(where, inExpression);
////        } else {
////            // 5. userDeptId为其他时，表示为员工级别没有下属机构，只能查看当前部门的数据
////            //  = 表达式
////            EqualsTo equalsTo = new EqualsTo(); // order_tbl.dept_id = userDeptId
////            equalsTo.setLeftExpression(new Column("order_tbl.dept_id"));
////            equalsTo.setRightExpression(new LongValue(userDeptId));
////            // 创建 AND 表达式 拼接Where 和 = 表达式
////            return new AndExpression(where, equalsTo); // WHERE user_id = 2 AND order_tbl.dept_id = 3
////        }
//    }
//}

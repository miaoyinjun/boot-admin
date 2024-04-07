package org.jjche.mybatis.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.base.BaseQueryCriteriaDTO;
import org.jjche.common.constant.EnumConstant;
import org.jjche.common.dto.PermissionDataRuleDTO;
import org.jjche.common.enums.InfraErrorCodeEnum;
import org.jjche.common.exception.util.AssertUtil;
import org.jjche.mybatis.param.BetweenParam;

import java.lang.reflect.Field;
import java.util.*;

/**
 * <p>
 * 工具类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-10-10
 */
public class MybatisUtil {


    /**
     * <p>
     * 组装排序queryWrapper
     * </p>
     *
     * @param query 查询参数
     * @return /
     */
    public static LambdaQueryWrapper assemblyLambdaQueryWrapper(BaseQueryCriteriaDTO query) {
        return assemblyLambdaQueryWrapper(query, null);
    }

    /**
     * <p>
     * 组装排序queryWrapper
     * </p>
     *
     * @param query    查询参数
     * @param sortEnum 排序枚举
     * @return /
     */
    public static LambdaQueryWrapper assemblyLambdaQueryWrapper(BaseQueryCriteriaDTO query,
                                                                Enum sortEnum) {
        QueryWrapper queryWrapper = assemblyQueryWrapper(query);
        //id DESC
        if (ObjectUtil.isNotNull(sortEnum)) {
            Class<? extends Enum<?>> clazz = (Class<? extends Enum<?>>) sortEnum.getClass();
            Map<String, Object> enumMap = EnumUtil.getNameFieldMap(clazz, EnumConstant.VALUE);
            String sortSql = enumMap.get(sortEnum.name()).toString();
            List<String> sortList = StrUtil.split(sortSql, StrPool.C_SPACE);
            if (CollUtil.size(sortList) == 2) {
                String columnName = CollUtil.getFirst(sortList);
                String sort = CollUtil.getLast(sortList);
                boolean isSort = StrUtil.endWithIgnoreCase(sortSql, "ASC")
                        || StrUtil.endWithIgnoreCase(sortSql, "DESC");
                if (isSort) {
                    if (StrUtil.equalsIgnoreCase(sort, "ASC")) {
                        queryWrapper.orderByAsc(columnName);
                    } else {
                        queryWrapper.orderByDesc(columnName);
                    }
                }
            }
        }
        return queryWrapper.lambda();
    }

    /**
     * <p>
     * 组装queryWrapper
     * </p>
     *
     * @param query 查询参数
     * @return /
     */
    @Deprecated
    public static QueryWrapper assemblyQueryWrapper(BaseQueryCriteriaDTO query) {
        QueryWrapper queryWrapper = Wrappers.query();
        //权限字段
        List<PermissionDataRuleDTO> permissionDataRuleList = query.getPermissionDataRuleList();
        if (CollUtil.isNotEmpty(permissionDataRuleList)) {
            for (PermissionDataRuleDTO dataRuleDTO : permissionDataRuleList) {
                String propName = dataRuleDTO.getColumn();
                QueryCriteria.Type condition = dataRuleDTO.getCondition();
                Object val = dataRuleDTO.getValue();
                setQueryWrapper(queryWrapper, condition, propName, val);
            }
        }
        //查询对象字段
        List<Field> fields = getAllFields(query.getClass(), new ArrayList<>());
        for (Field field : fields) {
            boolean accessible = field.isAccessible();
            // 设置对象的访问权限，保证对private的属性的访
            field.setAccessible(true);
            QueryCriteria q = field.getAnnotation(QueryCriteria.class);
            if (q != null) {
                String propName = q.propName();
                if (StrUtil.isBlank(propName)) {
                    propName = StrUtil.toUnderlineCase(field.getName());
                }
                Object val = null;
                try {
                    val = field.get(query);
                    if (Enum.class.isInstance(val)) {
                        Enum sEnum = (Enum) val;
                        Class<? extends Enum<?>> clazz = (Class<? extends Enum<?>>) sEnum.getClass();
                        Map<String, Object> enumMap = EnumUtil.getNameFieldMap(clazz, EnumConstant.VALUE);
                        val = enumMap.get(sEnum.name()).toString();
                    }
                } catch (Exception e) {
                    StaticLog.error(e.getMessage(), e);
                }
                if (ObjectUtil.isNull(val) || "".equals(val)) {
                    continue;
                }
                setQueryWrapper(queryWrapper, q.type(), propName, val);
            }
            field.setAccessible(accessible);
        }
        return queryWrapper;
    }

    /**
     * <p>
     * 设置查询对象
     * </p>
     *
     * @param queryWrapper 查询对象
     * @param type         类型
     * @param propName     属性名
     * @param val          值
     */
    private static void setQueryWrapper(QueryWrapper queryWrapper, QueryCriteria.Type type, String propName, Object val) {
        switch (type) {
            case EQUAL:
                queryWrapper.eq(propName, val);
                break;
            case NE:
                queryWrapper.ne(propName, val);
                break;
            case GT:
                queryWrapper.gt(propName, val);
                break;
            case GE:
                queryWrapper.ge(propName, val);
                break;
            case LT:
                queryWrapper.lt(propName, val);
                break;
            case LE:
                queryWrapper.le(propName, val);
                break;
            case LIKE:
                queryWrapper.like(propName, val);
                break;
            case LEFT_LIKE:
                queryWrapper.likeLeft(propName, val);
                break;
            case RIGHT_LIKE:
                queryWrapper.likeRight(propName, val);
                break;
            case BETWEEN:
                if (ObjectUtil.isNotNull(val)) {
                    List<String> betweenParamList = (List) val;
                    BetweenParam betweenParam = getBetweenParam(betweenParamList, true);
                    queryWrapper.between(propName, betweenParam.getStart(), betweenParam.getEnd());
                }
                break;
            case IN:
                if (ObjectUtil.isNotNull(val)) {
                    Set<String> inParamList = (Set<String>) val;
                    if (CollUtil.isNotEmpty(inParamList)) {
                        queryWrapper.in(propName, inParamList);
                    }
                }
                break;
            case IS_NULL:
                queryWrapper.isNull(propName);
                break;
            case NOT_NULL:
                queryWrapper.isNotNull(propName);
                break;
            case SQL_RULES:
                queryWrapper.apply(String.valueOf(val));
                break;
            default:
                break;
        }
    }


    /**
     * <p>
     * 获取class 所有字段
     * </p>
     *
     * @param clazz  类
     * @param fields 字段
     * @return /
     * @author miaoyj
     * @since 2020-10-10
     */
    private static List<Field> getAllFields(Class clazz, List<Field> fields) {
        if (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            getAllFields(clazz.getSuperclass(), fields);
        }
        return fields;
    }

    /**
     * <p>
     * List参数转换区间对象
     * </p>
     *
     * @param betweenParamList a {@link java.util.List} object.
     * @param isDateTime       a boolean.
     * @return a {@link BetweenParam} object.
     * @author miaoyj
     * @since 2020-10-15
     */
    public static BetweenParam getBetweenParam(List<String> betweenParamList, boolean isDateTime) {
        boolean isBetween = CollUtil.isNotEmpty(betweenParamList) && betweenParamList.size() == 2;
        AssertUtil.isTrue(isBetween, InfraErrorCodeEnum.COMMON_RANGE_PARAM_ERROR);
        BetweenParam betweenParam = new BetweenParam(betweenParamList.get(0), betweenParamList.get(1));
//        if (isDateTime) {
//            String dateTimeRegex = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$";
//            boolean isMatchStart = ReUtil.isMatch(dateTimeRegex, betweenParam.getStart());
//            boolean isMatchEnd = ReUtil.isMatch(dateTimeRegex, betweenParam.getEnd());
//            Assert.isTrue(isMatchStart && isMatchEnd, "区间参数日期时间格式不正确");
//        }
        return betweenParam;
    }
}

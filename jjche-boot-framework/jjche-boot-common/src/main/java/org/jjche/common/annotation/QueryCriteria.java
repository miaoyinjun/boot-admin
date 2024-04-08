package org.jjche.common.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 查询构造
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-10-10
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryCriteria {

    /**
     * <p>
     * 基本对象的属性名
     * </p>
     *
     * @return name
     */
    String propName() default "";

    /**
     * <p>
     * 查询方式
     * </p>
     *
     * @return type
     */
    Type type() default Type.EQUAL;

    @Getter
    @AllArgsConstructor
    enum Type {
        /**
         * 等于
         */
        EQUAL("EQUAL", "等于"),
        /**
         * 不等于
         */
        NE("NE", "不等于"),
        /**
         * 大于
         */
        GT("GT", "大于"),
        /**
         * 大于等于
         */
        GE("GE", "大于等于"),
        /**
         * 小于
         */
        LT("LT", "小于"),
        /**
         * 小于等于
         */
        LE("LE", "小于等于"),
        /**
         * 全模糊
         */
        LIKE("LIKE", "全模糊"),
        /**
         * 左模糊
         */
        LEFT_LIKE("LEFT_LIKE", "左模糊"),
        /**
         * 右模糊
         */
        RIGHT_LIKE("RIGHT_LIKE", "右模糊"),
        /**
         * 区间
         */
        BETWEEN("BETWEEN", "区间"),
        /**
         * 包含
         */
        IN("IN", "包含"),
        /**
         * 不为空
         */
        NOT_NULL("NOT_NULL", "不为空"),
        /**
         * 为空
         */
        IS_NULL("IS_NULL", "为空"),

        /**
         * 自定义SQL片段
         */
        SQL_RULES("USE_SQL_RULES", "自定义SQL片段"),
        ;

        /**
         * Constant <code>MAPPINGS</code>
         */
        private static final Map<String, Type> MAPPINGS;

        static {
            Map<String, Type> temp = new HashMap<String, Type>();
            for (Type courseEnum : values()) {
                temp.put(courseEnum.value, courseEnum);
            }
            MAPPINGS = Collections.unmodifiableMap(temp);
        }

        private final String value;
        private final String desc;

        /**
         * <p>
         * 根据index获取枚举
         * </p>
         *
         * @param index 索引
         * @return 枚举
         */
        public static Type resolve(String index) {
            return MAPPINGS.get(index);
        }

    }


}

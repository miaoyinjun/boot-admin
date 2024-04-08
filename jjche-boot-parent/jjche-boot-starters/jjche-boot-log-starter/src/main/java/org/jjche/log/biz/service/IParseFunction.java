package org.jjche.log.biz.service;

/**
 * <p>
 * 表达式方法调用接口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
public interface IParseFunction {

    /**
     * <p>
     * 是否在方法前运行
     * </p>
     *
     * @return /
     */
    default boolean executeBefore() {
        return false;
    }

    /**
     * <p>
     * 方法名
     * </p>
     *
     * @return 方法名
     */
    String functionName();

    /**
     * <p>
     * 方法回调
     * </p>
     *
     * @param value 参数
     * @return 结果
     */
    String apply(Object value);
}

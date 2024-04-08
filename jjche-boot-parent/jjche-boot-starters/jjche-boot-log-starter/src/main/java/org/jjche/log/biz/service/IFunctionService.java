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
public interface IFunctionService {

    /**
     * <p>
     * 方法回调
     * </p>
     *
     * @param functionName 方法名
     * @param value        参数
     * @return 结果
     */
    String apply(String functionName, Object value);

    /**
     * <p>
     * 是否方法前运行
     * </p>
     *
     * @param functionName 方法名
     * @return /
     */
    boolean beforeFunction(String functionName);
}

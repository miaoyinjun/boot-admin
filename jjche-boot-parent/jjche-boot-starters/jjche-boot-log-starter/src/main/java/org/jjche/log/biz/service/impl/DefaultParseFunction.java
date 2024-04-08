package org.jjche.log.biz.service.impl;


import org.jjche.log.biz.service.IParseFunction;

/**
 * <p>
 * 缺省表达式方法调用
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
public class DefaultParseFunction implements IParseFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean executeBefore() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String functionName() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String apply(Object value) {
        return null;
    }
}

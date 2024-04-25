package org.jjche.log.biz.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import org.jjche.common.util.StrUtil;
import org.jjche.log.biz.service.IFunctionService;
import org.jjche.log.biz.service.IParseFunction;

import java.util.List;

/**
 * <p>
 * 缺省表达式方法调用
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
public class DefaultFunctionServiceImpl implements IFunctionService {

    private final ParseFunctionFactory parseFunctionFactory;

    /**
     * <p>Constructor for DefaultFunctionServiceImpl.</p>
     *
     * @param parseFunctionFactory a {@link ParseFunctionFactory} object.
     */
    public DefaultFunctionServiceImpl(ParseFunctionFactory parseFunctionFactory) {
        this.parseFunctionFactory = parseFunctionFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String apply(String functionName, Object value) {
        IParseFunction function = parseFunctionFactory.getFunction(functionName);
        if (function == null) {
            //针对入参为Set<Long> ids这种，去除[]
            if (Iterable.class.isAssignableFrom(value.getClass())) {
                List list = Convert.toList(value);
                value = CollUtil.join(list, StrUtil.COMMA);
            }
            return value.toString();
        }
        return function.apply(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean beforeFunction(String functionName) {
        return parseFunctionFactory.isBeforeFunction(functionName);
    }
}

package com.boot.admin.log.biz.starter.support.aop;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * <p>
 * 配置日志注解方法
 * </p>
 *
 * @author miaoyj
 * @since 2021-04-30
 * @version 1.0.0-SNAPSHOT
 */
public class LogRecordPointcut extends StaticMethodMatcherPointcut implements Serializable {


    private LogRecordOperationSource logRecordOperationSource;

    /** {@inheritDoc} */
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return ObjectUtil.isNotNull(logRecordOperationSource.computeLogRecordOperation(method, targetClass));
    }

    void setLogRecordOperationSource(LogRecordOperationSource logRecordOperationSource) {
        this.logRecordOperationSource = logRecordOperationSource;
    }
}

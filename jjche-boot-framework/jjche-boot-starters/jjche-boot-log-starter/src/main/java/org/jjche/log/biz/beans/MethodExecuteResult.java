package org.jjche.log.biz.beans;

import lombok.Getter;

import java.lang.reflect.Method;

/**
 * @author wulang
 **/
@Getter
public class MethodExecuteResult {
    private final Method method;
    private final Object[] args;
    private final Class<?> targetClass;
    private boolean success;
    private Throwable throwable;
    private String errorMsg;
    private Object result;

    public MethodExecuteResult(Method method, Object[] args, Class<?> targetClass) {
        this.method = method;
        this.args = args;
        this.targetClass = targetClass;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}

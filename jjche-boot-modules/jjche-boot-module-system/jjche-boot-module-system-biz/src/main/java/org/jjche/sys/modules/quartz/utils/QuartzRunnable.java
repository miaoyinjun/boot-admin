package org.jjche.sys.modules.quartz.utils;

import org.jjche.common.util.StrUtil;
import org.jjche.core.util.SpringContextHolder;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * 执行定时任务
 *
 * @author /
 * @version 1.0.8-SNAPSHOT
 */
public class QuartzRunnable implements Callable {

    private final Object target;
    private final Method method;
    private final String params;

    QuartzRunnable(String beanName, String methodName, String params)
            throws NoSuchMethodException, SecurityException {
        this.target = SpringContextHolder.getBean(beanName);
        this.params = params;

        if (StrUtil.isNotBlank(params)) {
            this.method = target.getClass().getDeclaredMethod(methodName, String.class);
        } else {
            this.method = target.getClass().getDeclaredMethod(methodName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object call() throws Exception {
        ReflectionUtils.makeAccessible(method);
        if (StrUtil.isNotBlank(params)) {
            method.invoke(target, params);
        } else {
            method.invoke(target);
        }
        return null;
    }
}

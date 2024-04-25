package org.jjche.cat.annotation;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.StaticLog;
import com.dianping.cat.Cat;
import org.jjche.common.util.StrUtil;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * <p>
 * 计数 统计
 * </p>
 *
 * @author miaoyj
 * @since 2022-10-19
 */
public class MetricForCountAnnotationProcessor implements BeanPostProcessor, PriorityOrdered {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
        Map<Method, CatMetricForCount> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
                new MethodIntrospector.MetadataLookup<CatMetricForCount>() {
                    @Override
                    public CatMetricForCount inspect(Method method) {
                        return AnnotationUtils.getAnnotation(method, CatMetricForCount.class);
                    }
                });
        if (CollUtil.isEmpty(annotatedMethods)) {
            return bean;
        } else {
            for (Map.Entry<Method, CatMetricForCount> entry : annotatedMethods.entrySet()) {
                return processMetricForCount(bean, entry.getKey(), entry.getValue());
            }
        }
        return bean;
    }

    private Object processMetricForCount(Object bean, Method method, CatMetricForCount metricForCount) {
        if (StrUtil.isEmpty(metricForCount.value())) {
            StaticLog.warn("@CatMetricForCount annotation on " + method.getName() + " name can't be null or empty");
            return bean;
        }
        if (metricForCount.count() <= 0) {
            StaticLog.warn("@CatMetricForCount annotation on " + method.getName() + " value can't be zero or negative");
            return bean;
        }
        ProxyFactory factory = new ProxyFactory();
        factory.setTarget(bean);
        factory.addAdvice(new MetricLoggerAdvice(metricForCount));
        return factory.getProxy();
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    private static class MetricLoggerAdvice implements AfterReturningAdvice {

        private CatMetricForCount metricForCount;

        public MetricLoggerAdvice(CatMetricForCount metricForCount) {
            this.metricForCount = metricForCount;
        }

        @Override
        public void afterReturning(Object returnValue, Method method, Object[] args, Object target) {
            AnnotationMethodMatcher matcher = new AnnotationMethodMatcher(CatMetricForCount.class);
            if (matcher.matches(method, target.getClass())) {
                Cat.logMetricForCount(metricForCount.value(), metricForCount.count());
            }
        }
    }
}

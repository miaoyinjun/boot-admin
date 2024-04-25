package org.jjche.cat.annotation;

import cn.hutool.core.collection.CollUtil;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.jjche.cat.constant.CatConstant;
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
 * 方法 统计
 * </p>
 *
 * @author miaoyj
 * @since 2022-10-19
 */
public class CatTransactionAnnotationProcessor implements BeanPostProcessor, PriorityOrdered {


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
        Map<Method, CatTransaction> annotatedMethods = MethodIntrospector.selectMethods(targetClass, new MethodIntrospector.MetadataLookup<CatTransaction>() {
            @Override
            public CatTransaction inspect(Method method) {
                return AnnotationUtils.getAnnotation(method, CatTransaction.class);
            }
        });
        if (CollUtil.isEmpty(annotatedMethods)) {
            return bean;
        } else {
            for (Map.Entry<Method, CatTransaction> entry : annotatedMethods.entrySet()) {
                return processCatTransaction(bean, entry.getValue());
            }
        }
        return bean;
    }

    private Object processCatTransaction(Object bean, CatTransaction catTransaction) {
        ProxyFactory factory = new ProxyFactory();
        factory.setTarget(bean);
        factory.addAdvice(new CatTransactionAdvice(catTransaction));
        return factory.getProxy();
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    private static class CatTransactionAdvice implements AfterReturningAdvice {

        private CatTransaction catTransaction;

        public CatTransactionAdvice(CatTransaction catTransaction) {
            this.catTransaction = catTransaction;
        }

        @Override
        public void afterReturning(Object returnValue, Method method, Object[] args, Object target) {
            AnnotationMethodMatcher matcher = new AnnotationMethodMatcher(CatTransaction.class);
            if (matcher.matches(method, target.getClass())) {
                String clsName = method.getDeclaringClass().getSimpleName();
                Transaction t = Cat.newTransaction(CatConstant.TYPE_METHOD, clsName + "." + method.getName());
                t.setSuccessStatus();
                t.complete();
            }
        }
    }
}

package org.jjche.cat.annotation;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * <p>
 * 注解 注册
 * </p>
 *
 * @author miaoyj
 * @since 2022-10-19
 */
public class AnnotationProcessorRegister implements ImportBeanDefinitionRegistrar {

    private final BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
       register(MetricForCountAnnotationProcessor.class, registry);
       register(CatTransactionAnnotationProcessor.class, registry);
    }

    private void register(Class<?> beanClass, BeanDefinitionRegistry registry) {
        BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(beanClass).getBeanDefinition();
        String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
        registry.registerBeanDefinition(beanName, beanDefinition);
    }
}

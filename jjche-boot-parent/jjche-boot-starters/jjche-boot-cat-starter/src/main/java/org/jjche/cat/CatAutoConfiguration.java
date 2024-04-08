package org.jjche.cat;

import com.dianping.cat.Cat;
import org.jjche.cat.annotation.AnnotationProcessorRegister;
import org.jjche.cat.conf.CatClientConfigProvider;
import org.jjche.cat.property.JjcheCatProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 * 入口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-10
 */
@Import({AnnotationProcessorRegister.class})
@Configuration
public class CatAutoConfiguration implements InitializingBean {
    @Autowired
    private JjcheCatProperties catProperties;

    @Override
    public void afterPropertiesSet() {
        Boolean enabled = catProperties.getEnabled();
        if (enabled) {
            CatClientConfigProvider.setCatProperties(catProperties);
            Cat.initialize();
        }
    }
}
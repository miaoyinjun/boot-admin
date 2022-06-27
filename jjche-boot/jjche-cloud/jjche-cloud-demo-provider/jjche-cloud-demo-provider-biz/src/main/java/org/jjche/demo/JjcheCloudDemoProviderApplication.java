package org.jjche.demo;

import org.jjche.core.util.SpringContextHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 入口
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-06-18 9:16
 */
@SpringBootApplication
public class JjcheCloudDemoProviderApplication {
    /**
     * 入口
     *
     * @param args an array of {@link java.lang.String} objects.
     * @author miaoyj
     * @since 2020-07-09
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(JjcheCloudDemoProviderApplication.class, args);
        SpringContextHolder.appLog(application);
    }
}

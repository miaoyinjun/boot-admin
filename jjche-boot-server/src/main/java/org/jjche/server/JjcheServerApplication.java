package org.jjche.server;

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
public class JjcheServerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(JjcheServerApplication.class, args);
        SpringContextHolder.appLog(application);
    }
}

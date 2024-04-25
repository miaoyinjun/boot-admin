package org.jjche.cat.conf;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.jjche.cat.integration.logback.CatLogbackAppender;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * <p>
 * logback 增加cat
 * </p>
 *
 * @author miaoyj
 * @since 2022-10-19
 */
@Configuration
public class LogbackAutoConfig {

    @PostConstruct
    public void afterPropertiesSet() {
        LoggerContext lc = (LoggerContext) StaticLoggerBinder.getSingleton().getLoggerFactory();
        CatLogbackAppender ca = new CatLogbackAppender();
        ca.setContext(lc);
        ca.setName("CatLogbackAppender");
        ca.start();
        Logger rootLogger = lc.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(ca);
    }
}

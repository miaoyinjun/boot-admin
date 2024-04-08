package org.jjche.cache.conf;

import cn.hutool.core.text.CharSequenceUtil;
import org.jjche.common.constant.SpringPropertyConstant;
import org.jjche.common.util.StrUtil;
import org.redisson.api.NameMapper;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * Redisson配置
 * </p>
 *
 * @author miaoyj
 * @since 2023-02-06
 */
@Configuration
public class RedissonConfig {
    @Value("${" + SpringPropertyConstant.APP_NAME + "}")
    private String appName;

    @Bean
    public RedissonAutoConfigurationCustomizer redissonAutoConfigurationCustomizer() {
        return cfg -> {
            /**
             * 兼容redisson有/无密码报错问题
             * 密码为空时将密码设置为null，这样就会走无密码认证
             */
            if (StrUtil.isEmpty(cfg.useSingleServer().getPassword())) {
                cfg.useSingleServer().setPassword(null);
            }
            //key自定义前缀
            NameMapper nameMapper = new NameMapper() {
                @Override
                public String map(String name) {
                    return appName + ":" + name;
                }

                @Override
                public String unmap(String name) {
                    return CharSequenceUtil.subAfter(name, appName + ":", false);
                }
            };
            if (cfg.isClusterConfig()) {
                cfg.useClusterServers().setNameMapper(nameMapper);
            } else if (cfg.isSentinelConfig()) {
                cfg.useSentinelServers().setNameMapper(nameMapper);
            } else {
                cfg.useSingleServer().setNameMapper(nameMapper);
            }
        };
    }
}
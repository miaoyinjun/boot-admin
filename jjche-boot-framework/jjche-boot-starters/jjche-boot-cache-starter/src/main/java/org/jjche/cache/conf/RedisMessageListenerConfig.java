package org.jjche.cache.conf;

import lombok.AllArgsConstructor;
import org.jjche.common.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.List;

/**
 * <p>
 * Redis消息临听配置
 * </p>
 *
 * @author miaoyj
 * @since 2023-02-07
 */
@Configuration
@AllArgsConstructor
public class RedisMessageListenerConfig {
    private final transient List<RedisSubscriber> subscriptorList;

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {

        //创建一个消息监听对象
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        //将监听对象放入到容器中
        container.setConnectionFactory(connectionFactory);

        if (this.subscriptorList != null && this.subscriptorList.size() > 0) {
            for (RedisSubscriber subscriber : this.subscriptorList) {

                if (subscriber == null || StrUtil.isBlank(subscriber.getTopic())) {
                    continue;
                }
                //一个订阅者对应一个主题通道信息
                container.addMessageListener(subscriber, new PatternTopic(subscriber.getTopic()));
            }
        }

        return container;
    }
}

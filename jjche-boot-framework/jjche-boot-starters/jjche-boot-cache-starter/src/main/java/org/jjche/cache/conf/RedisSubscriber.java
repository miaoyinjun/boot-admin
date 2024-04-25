package org.jjche.cache.conf;


import org.springframework.data.redis.connection.MessageListener;

/**
 * <p>
 * 订阅者接收消息的基类
 * </p>
 *
 * @author miaoyj
 * @since 2023-02-07
 */
public interface RedisSubscriber extends MessageListener {

    /**
     * 类型
     *
     * @return
     */
    default String getType() {
        return this.getClass().getSimpleName();
    }

    /**
     * 通道名称
     *
     * @return
     */
    String getTopic();

}
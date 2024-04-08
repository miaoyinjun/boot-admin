package org.jjche.cache.conf;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 事务注解问题
 * </p>
 * <p>
 * https://my.oschina.net/lasipia/blog/967685
 * https://my.oschina.net/lasipia/blog/1002640
 * https://www.cnblogs.com/fantjesse/p/12206688.html
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-03-04
 */
public class RedisTemplateDelegate<K, V> extends RedisTemplate<K, V> {
    @Resource(name = "notSupportTransactionRedisTemplate")
    private RedisTemplate<K, V> notSupportTransactionRedisTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T execute(RedisCallback<T> action, boolean exposeConnection, boolean pipeline) {
        //判断是否有@Transactional注解，如果有就用支持事务的RedisTemplate
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            return super.execute(action, exposeConnection, pipeline);
        } else {
            return notSupportTransactionRedisTemplate.execute(action, exposeConnection, pipeline);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T execute(SessionCallback<T> session) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            return super.execute(session);
        } else {
            return notSupportTransactionRedisTemplate.execute(session);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Object> executePipelined(final SessionCallback<?> session, final RedisSerializer<?> resultSerializer) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            return super.executePipelined(session, resultSerializer);
        } else {
            return notSupportTransactionRedisTemplate.executePipelined(session, resultSerializer);
        }
    }
}

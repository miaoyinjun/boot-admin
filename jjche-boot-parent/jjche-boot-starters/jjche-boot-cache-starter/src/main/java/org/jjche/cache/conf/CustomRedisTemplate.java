package org.jjche.cache.conf;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class CustomRedisTemplate<K, V> extends RedisTemplate<K, V> {
    private boolean enableTransactionSupport = false;

    private static boolean isActualNonReadonlyTransactionActive() {
        return TransactionSynchronizationManager.isActualTransactionActive()
                && !TransactionSynchronizationManager.isCurrentTransactionReadOnly();
    }

    public static RedisConnection getConnection(RedisConnection connection, boolean existingConnection, RedisConnectionFactory connectionFactory, boolean enableTransactionSupport) {
        if (existingConnection && !Proxy.isProxyClass(connection.getClass()) && isActualNonReadonlyTransactionActive()) {
            RedisConnectionUtils.unbindConnection(connectionFactory);
            List<TransactionSynchronization> list = new ArrayList<>(TransactionSynchronizationManager.getSynchronizations());
            TransactionSynchronizationManager.clearSynchronization();
            TransactionSynchronizationManager.initSynchronization();
            //移除最后一个回调（由于之前回去连接是会注册一个事务回调，下面如果再获取连接会导致注册两个事务回调。事务完成后会执行两次回调，
            //回调中会清除资源，第一次已经清除，第二次再清的时候回抛出异常）
            list.remove(list.size() - 1);
            list.forEach(TransactionSynchronizationManager::registerSynchronization);
            connection = RedisConnectionUtils.bindConnection(connectionFactory, enableTransactionSupport);
        }
        return connection;
    }

    /**
     * 解决 redis先非事务中运行，然后又在事务中运行，出现取到的连接还是非事务连接的问题
     * 在事务环境中用非事务连接，读取操作无法马上读出数据
     *
     * @param connection
     * @param existingConnection
     * @return
     */
    @Override
    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
        return getConnection(connection, existingConnection, getConnectionFactory(), enableTransactionSupport);
    }

    @Override
    public void setEnableTransactionSupport(boolean enableTransactionSupport) {
        super.setEnableTransactionSupport(enableTransactionSupport);
        this.enableTransactionSupport = enableTransactionSupport;
    }
}

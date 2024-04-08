package org.jjche.cache.conf;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;

public class CustomStringRedisTemplate extends StringRedisTemplate {
    private boolean enableTransactionSupport = false;

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
        return CustomRedisTemplate.getConnection(connection, existingConnection, getConnectionFactory(), enableTransactionSupport);
    }

    @Override
    public void setEnableTransactionSupport(boolean enableTransactionSupport) {
        super.setEnableTransactionSupport(enableTransactionSupport);
        this.enableTransactionSupport = enableTransactionSupport;
    }
}

package org.jjche.cache.conf;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.jjche.common.constant.SpringPropertyConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * <p>CustomPrefixKeyStringSerializer class.</p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 */
@Component
public class CustomPrefixKeyStringSerializer implements RedisSerializer<String> {

    private final Charset charset;
    @Value("${" + SpringPropertyConstant.APP_NAME + "}")
    private String appName;

    /**
     * <p>Constructor for CustomPrefixKeyStringSerializer.</p>
     */
    public CustomPrefixKeyStringSerializer() {
        this(Charset.forName("UTF8"));
    }

    /**
     * <p>Constructor for CustomPrefixKeyStringSerializer.</p>
     *
     * @param charset a {@link java.nio.charset.Charset} object.
     */
    public CustomPrefixKeyStringSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String deserialize(byte[] bytes) {
        String saveKey = new String(bytes, charset);
        int indexOf = saveKey.indexOf(appName);
        saveKey = saveKey.substring(indexOf);
        return (saveKey.getBytes() == null ? null : saveKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] serialize(String string) {
        String key = StrUtil.format(appName + ":{}", string);
        return (key == null ? null : key.getBytes(charset));
    }
}

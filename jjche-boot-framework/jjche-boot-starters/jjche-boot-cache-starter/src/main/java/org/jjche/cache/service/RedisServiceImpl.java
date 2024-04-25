package org.jjche.cache.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.jjche.common.constant.SpringPropertyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * Redis操作实现
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-20
 */
@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${" + SpringPropertyConstant.APP_NAME + "}")
    private String appName;

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean hashHasHk(String hKey, String hashKey) {
        return redisTemplate.opsForHash().hasKey(hKey, hashKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T hashGet(String hKey, String hashKey, Class<T> valueCls) {
        Object result = redisTemplate.opsForHash().get(hKey, hashKey);
        if (result != null) {
            return Convert.convert(valueCls, result);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Object, Object> hashGetAll(String key, Class valueCls) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        if (MapUtil.isNotEmpty(map)) {
            return Convert.toMap(Object.class, valueCls, map);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long hashIncrementLongOfHashMap(String hKey, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(hKey, hashKey, delta);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double hashIncrementDoubleOfHashMap(String hKey, String hashKey, Double delta) {
        return redisTemplate.opsForHash().increment(hKey, hashKey, delta);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hashPushHashMap(String key, Object hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hashPushHashMap(String key, Map<String, Object> maps) {
        redisTemplate.opsForHash().putAll(key, maps);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Object> hashGetAllHashKey(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long hashGetHashMapSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Object> hashGetHashAllValues(String key, Class valueCls) {
        List<Object> result = redisTemplate.opsForHash().values(key);
        if (CollUtil.isNotEmpty(result)) {
            return Convert.toList(valueCls, result);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long hashDeleteHashKey(String key, Object... hashKeys) {
        if (hashKeys.length > 0) {
            return redisTemplate.opsForHash().delete(key, hashKeys);
        }
        return 0L;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Object> listGet(String key, Class valueCls) {
        return this.listRangeList(key, 0L, -1L, valueCls);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long listLeftPush(String key, Object... values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long listLeftPushAll(String key, Collection<Object> values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long listRightPush(String key, Object... values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long listRightPushAll(String key, Collection<Object> values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T listRightPop(String key, Class<T> valueCls) {
        Object result = redisTemplate.opsForList().rightPop(key);
        if (ObjectUtil.isNotNull(result)) {
            return Convert.convert(valueCls, result);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T listLeftPop(String key, Class<T> valueCls) {
        Object result = redisTemplate.opsForList().leftPop(key);
        if (ObjectUtil.isNotNull(result)) {
            return Convert.convert(valueCls, result);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long listSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Object> listRangeList(String key, Long start, Long end, Class valueCls) {
        List<Object> result = redisTemplate.opsForList().range(key, start, end);
        if (CollUtil.isNotEmpty(result)) {
            result = Convert.toList(valueCls, result);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T listIndexFromList(String key, Long index, Class<T> valueCls) {
        Object result = redisTemplate.opsForList().index(key, index);
        if (ObjectUtil.isNotNull(result)) {
            return Convert.convert(valueCls, result);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void listSetValueToList(String key, Long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void listTrimByRange(String key, Long start, Long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long setAddSetObject(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long setGetSizeForSetMap(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Object> setGetMemberOfSetMap(String key, Class valueCls) {
        Set<Object> result = redisTemplate.opsForSet().members(key);
        if (CollUtil.isNotEmpty(result)) {
            result = CollUtil.newHashSet(Convert.toList(valueCls, result));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean setCheckIsMemberOfSet(String key, Object o) {
        return redisTemplate.opsForSet().isMember(key, o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T setPop(String key, Class<T> valueCls) {
        Object result = redisTemplate.opsForSet().pop(key);
        if (ObjectUtil.isNotNull(result)) {
            return Convert.convert(valueCls, result);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long setRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stringAppendString(String key, String value) {
        stringRedisTemplate.opsForValue().append(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String stringGetString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long stringIncrementLongString(String key, Long delta) {
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double stringIncrementDoubleString(String key, Double delta) {
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stringSetString(String key, String value) {
        stringSetString(key, value, 0L);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stringSetString(String key, String value, Long expireMilliSeconds) {
        if (expireMilliSeconds > 0) {
            stringRedisTemplate.opsForValue().set(key, value, expireMilliSeconds, TimeUnit.MILLISECONDS);
        } else {
            stringRedisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String stringGetAndSet(String key, String value) {
        return stringRedisTemplate.opsForValue().getAndSet(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void objectSetObject(String key, Object o) {
        this.objectSetObject(key, o, 0L);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void objectSetObject(String key, Object o, Long expireMilliSeconds) {
        if (expireMilliSeconds > 0) {
            redisTemplate.opsForValue().set(key, o, expireMilliSeconds, TimeUnit.MILLISECONDS);
        } else {
            redisTemplate.opsForValue().set(key, o);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T objectGetObject(String key, Class<T> valueCls) {
        Object result = redisTemplate.opsForValue().get(key);
        if (result != null) {
            return Convert.convert(valueCls, result);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean setExpire(String key, Long expireMilliSeconds) {
        return redisTemplate.expire(key, expireMilliSeconds, TimeUnit.MILLISECONDS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> keys(String pattern) {
        Set<String> keyList = redisTemplate.keys(pattern + "*");
        String keyPrefix = new StringBuffer(appName).append(":").toString();
        return keyList.stream().map(s -> StrUtil.removePrefix(s, keyPrefix)).collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean delete(Set<String> keys) {
        Long successSize = redisTemplate.delete(keys);
        return ObjectUtil.isNotNull(successSize) && successSize == keys.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delByKeys(String prefix, Set ids) {
        Set<String> keys = new HashSet<>();
        for (Object id : ids) {
            CollUtil.addAll(keys, new StringBuffer(prefix).append(id).toString());
        }
        this.delete(keys);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delByKeyPrefix(String prefix) {
        Set<String> keys = this.keys(prefix);
        return this.delete(keys);
    }

    @Override
    public <T> T execute(RedisScript<T> script, List keys, Object... args) {
        return (T) redisTemplate.execute(script, keys, args);
    }

    @Override
    public void push(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }
}

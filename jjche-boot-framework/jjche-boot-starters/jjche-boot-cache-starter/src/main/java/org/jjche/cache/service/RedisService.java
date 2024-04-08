package org.jjche.cache.service;

import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Redis操作定义
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-20
 */
public interface RedisService {

    /**
     * <p>
     * 添加键值对到哈希表key中
     * </p>
     *
     * @param key     标识
     * @param hashKey 哈希标识
     * @param value   值
     */
    void hashPushHashMap(String key, Object hashKey, Object value);

    /**
     * <p>
     * 添加map到哈希表key中
     * </p>
     *
     * @param key  标识
     * @param maps kv
     */
    void hashPushHashMap(String key, Map<String, Object> maps);

    /**
     * <p>
     * 查看哈希hKey中hashKey是否存在
     * </p>
     *
     * @param hKey    标识
     * @param hashKey 哈希标识
     * @return 是否存在
     */
    Boolean hashHasHk(String hKey, String hashKey);

    /**
     * <p>
     * 查询哈希表 hKey 中给定域 hashKey 的值
     * </p>
     *
     * @param hKey     标识
     * @param hashKey  哈希标识
     * @param valueCls value类型
     * @param <T>      a T object.
     * @return 对象
     */
    <T> T hashGet(String hKey, String hashKey, Class<T> valueCls);

    /**
     * <p>
     * 获取所有的散列值
     * </p>
     *
     * @param key      标识
     * @param valueCls value类型
     * @return 所有的散列值
     */
    Map<Object, Object> hashGetAll(String key, Class valueCls);

    /**
     * <p>
     * 哈希表 hKey 中的域 hashKey 的值加上增量 delta
     * </p>
     *
     * @param hKey    标识
     * @param hashKey 哈希标识
     * @param delta   数值
     * @return 哈希表 hKey 中域 hashKey 的值
     */
    Long hashIncrementLongOfHashMap(String hKey, String hashKey, Long delta);

    /**
     * <p>
     * 哈希表 hKey 中的域 hashKey 的值加上浮点值 增量 delta
     * </p>
     *
     * @param hKey    标识
     * @param hashKey 哈希标识
     * @param delta   数值
     * @return 哈希表 hKey 中域 hashKey 的值
     */
    Double hashIncrementDoubleOfHashMap(String hKey, String hashKey, Double delta);

    /**
     * <p>
     * 获取哈希表key中的所有域
     * </p>
     *
     * @param key 标识
     * @return 所有域
     */
    Set<Object> hashGetAllHashKey(String key);

    /**
     * <p>
     * 获取哈希中的字段数量
     * </p>
     *
     * @param key 标识
     * @return 字段数量
     */
    Long hashGetHashMapSize(String key);

    /**
     * <p>
     * 获取哈希中的所有值
     * </p>
     *
     * @param key      标识
     * @param valueCls value类型
     * @return 所有值
     */
    List<Object> hashGetHashAllValues(String key, Class valueCls);

    /**
     * <p>
     * 删除一个或多个哈希字段
     * </p>
     *
     * @param key      标识
     * @param hashKeys 哈希标识
     * @return 被成功删除的数量
     */
    Long hashDeleteHashKey(String key, Object... hashKeys);

    /**
     * <p>
     * 批量把一个数组从左插入到列表中
     * </p>
     *
     * @param key    标识
     * @param values 内容
     * @return 总数量
     */
    Long listLeftPush(String key, Object... values);

    /**
     * <p>
     * 批量把一个集合从左插入到列表中
     * </p>
     *
     * @param key    标识
     * @param values 内容
     * @return 总数量
     */
    Long listLeftPushAll(String key, Collection<Object> values);

    /**
     * <p>
     * 批量把一个数组从右插入到列表中
     * </p>
     *
     * @param key    标识
     * @param values 内容
     * @return 总数量
     */
    Long listRightPush(String key, Object... values);

    /**
     * <p>
     * 批量把一个数组从右插入到列表中
     * </p>
     *
     * @param key    标识
     * @param values 内容
     * @return 总数量
     */
    Long listRightPushAll(String key, Collection<Object> values);

    /**
     * <p>
     * 列表获取
     * </p>
     *
     * @param key      标识
     * @param valueCls value类型
     * @return 列表
     */
    List<Object> listGet(String key, Class valueCls);

    /**
     * <p>
     * 从右出栈
     * </p>
     *
     * @param key      标识
     * @param valueCls value类型
     * @param <T>      a T object.
     * @return 弹出的对象
     */
    <T> T listRightPop(String key, Class<T> valueCls);

    /**
     * <p>
     * 从左出栈
     * </p>
     *
     * @param key      标识
     * @param valueCls value类型
     * @param <T>      a T object.
     * @return 弹出的对象
     */
    <T> T listLeftPop(String key, Class<T> valueCls);

    /**
     * <p>
     * 集合list的长度
     * </p>
     *
     * @param key 标识
     * @return 长度
     */
    Long listSize(String key);

    /**
     * <p>
     * 查询列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定
     * </p>
     *
     * @param key      标识
     * @param start    开始位置
     * @param end      结束位置
     * @param valueCls value类型
     * @return 列表
     */
    List<Object> listRangeList(String key, Long start, Long end, Class valueCls);

    /**
     * <p>
     * 根据下标查询list中某个值
     * </p>
     *
     * @param key      标识
     * @param index    位置
     * @param valueCls value类型
     * @param <T>      a T object.
     * @return 对象
     */
    <T> T listIndexFromList(String key, Long index, Class<T> valueCls);

    /**
     * <p>
     * 根据下标设置value
     * </p>
     *
     * @param key   标识
     * @param index 位置
     * @param value 值
     */
    void listSetValueToList(String key, Long index, Object value);

    /**
     * <p>
     * 裁剪(删除), 删除 除了[start,end]以外的所有元素
     * </p>
     *
     * @param key   标识
     * @param start 开始位置
     * @param end   结束位置
     */
    void listTrimByRange(String key, Long start, Long end);

    /**
     * <p>
     * 将一个或多个 value 元素加入到集合 key 当中，已经存在于集合的 value 元素将被忽略
     * </p>
     *
     * @param key    标识
     * @param values 值
     * @return 被添加到集合中的新元素的数量，不包括被忽略的元素
     */
    Long setAddSetObject(String key, Object... values);

    /**
     * <p>
     * 获取set集合的大小
     * </p>
     *
     * @param key 标识
     * @return 集合大小
     */
    Long setGetSizeForSetMap(String key);

    /**
     * <p>
     * 获取set集合
     * </p>
     *
     * @param key      标识
     * @param valueCls value类型
     * @return 集合
     */
    Set<Object> setGetMemberOfSetMap(String key, Class valueCls);

    /**
     * <p>
     * 检查元素是不是set集合中的
     * </p>
     *
     * @param key 标识
     * @param o   对象
     * @return 是否存在
     */
    Boolean setCheckIsMemberOfSet(String key, Object o);

    /**
     * <p>
     * 出栈
     * </p>
     *
     * @param key      标识
     * @param valueCls value类型
     * @param <T>      a T object.
     * @return 出栈内容
     */
    <T> T setPop(String key, Class<T> valueCls);

    /**
     * <p>
     * 删除
     * </p>
     *
     * @param key    标识
     * @param values 内容
     * @return 删除长度
     */
    Long setRemove(String key, Object... values);

    /**
     * <p>
     * 设置键的字符串值
     * </p>
     *
     * @param key   标识
     * @param value 值
     */
    void stringSetString(String key, String value);

    /**
     * <p>
     * 设置键的字符串值
     * </p>
     *
     * @param key                标识
     * @param value              值
     * @param expireMilliSeconds 过期时间(毫秒)
     */
    void stringSetString(String key, String value, Long expireMilliSeconds);

    /**
     * <p>
     * 设置键的字符串值并返回其旧值
     * </p>
     *
     * @param key   标识
     * @param value 值
     * @return 旧值
     */
    String stringGetAndSet(String key, String value);

    /**
     * <p>
     * 追加字符串
     * </p>
     * <p>
     * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。
     * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样
     * </p>
     *
     * @param key   标识
     * @param value 值
     */
    void stringAppendString(String key, String value);

    /**
     * <p>
     * 获取指定键的值
     * </p>
     *
     * @param key 标识
     * @return 键的值
     */
    String stringGetString(String key);

    /**
     * <p>
     * 将键的整数值按给定的长整型数值增加
     * </p>
     *
     * @param key   标识
     * @param delta 值
     * @return 增长后的结果值
     */
    Long stringIncrementLongString(String key, Long delta);

    /**
     * <p>
     * 将键的整数值按给定的浮点型数值增加
     * </p>
     *
     * @param key   标识
     * @param delta 值
     * @return 返回增长后的结果值
     */
    Double stringIncrementDoubleString(String key, Double delta);

    /**
     * <p>
     * 设置对象
     * </p>
     *
     * @param key 标识
     * @param o   对象
     */
    void objectSetObject(String key, Object o);

    /**
     * <p>
     * 设置对象
     * </p>
     *
     * @param key                标识
     * @param o                  对象
     * @param expireMilliSeconds 过期时间(毫秒)
     */
    void objectSetObject(String key, Object o, Long expireMilliSeconds);

    /**
     * <p>
     * 获取对象
     * </p>
     *
     * @param key      标识
     * @param valueCls value类型
     * @param <T>      a T object.
     * @return 对象
     */
    <T> T objectGetObject(String key, Class<T> valueCls);

    /**
     * <p>
     * 删除
     * </p>
     *
     * @param key 标识
     * @return 是否成功
     */
    Boolean delete(String key);

    /**
     * <p>
     * 获取过期时间（毫秒）
     * </p>
     *
     * @param key 标识
     * @return 过期时间（毫秒）
     */
    Long getExpire(String key);

    /**
     * <p>
     * key是否存在
     * </p>
     *
     * @param key 标识
     * @return 是否存在
     */
    Boolean hasKey(String key);

    /**
     * <p>
     * 设置过期时间
     * </p>
     *
     * @param key                标识
     * @param expireMilliSeconds 过期时间(毫秒)
     * @return 是否成功
     */
    Boolean setExpire(String key, Long expireMilliSeconds);

    /**
     * <p>
     * 查找匹配key
     * </p>
     *
     * @param pattern a {@link java.lang.String} object.
     * @return a {@link java.util.Set} object.
     */
    Set<String> keys(String pattern);

    /**
     * <p>
     * 删除
     * </p>
     *
     * @param keys 标识
     * @return 是否成功
     */
    Boolean delete(Set<String> keys);

    /**
     * <p>
     * 批量删除
     * </p>
     *
     * @param prefix 前缀
     * @param ids    id
     */
    void delByKeys(String prefix, Set ids);

    /**
     * <p>
     * 批量删除key前缀
     * </p>
     *
     * @param prefix 前缀
     * @return 是否成功
     */
    boolean delByKeyPrefix(String prefix);

    /**
     * <p>
     * 运行脚本
     * </p>
     *
     * @param script /
     * @param keys   /
     * @param args   /
     * @return /
     */
    <T> T execute(RedisScript<T> script, List keys, Object... args);

    /**
     * <p>
     * 发布订阅
     * </p>
     *
     * @param channel /
     * @param message /
     */
    void push(String channel, Object message);
}

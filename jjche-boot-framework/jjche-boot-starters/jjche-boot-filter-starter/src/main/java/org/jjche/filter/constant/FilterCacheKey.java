package org.jjche.filter.constant;

/**
 * <p>
 * 过滤器缓存
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-06
 */
public interface FilterCacheKey {
    /**
     * 限流脚本
     */
    String SCRIPT_LUA_LIMIT = "local c" +
            "\nc = redis.call('get',KEYS[1])" +
            "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
            "\nreturn c;" +
            "\nend" +
            "\nc = redis.call('incr',KEYS[1])" +
            "\nif tonumber(c) == 1 then" +
            "\nredis.call('expire',KEYS[1],ARGV[2])" +
            "\nend" +
            "\nreturn c;";

    /**
     * 限速
     */
    String REQUEST_LIMIT = "sys:request:limit:";
}

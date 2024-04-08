package org.jjche.common.context;

import cn.hutool.core.convert.Convert;
import com.alibaba.ttl.TransmittableThreadLocal;
import org.jjche.common.constant.LogConstant;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.util.StrUtil;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * <p>
 * 获取当前线程变量中的 用户信息
 * --参考lamp-util
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-11
 */
public final class ContextUtil {
    private static final ThreadLocal<Map<String, String>> THREAD_LOCAL = new TransmittableThreadLocal<>();

    private ContextUtil() {
    }

    /**
     * <p>
     * 获取应用加密密钥
     * </p>
     *
     * @return /
     */
    public static String getAppKeyEncKey() {
        return get(SecurityConstant.APP_KEY_ENC_KEY, String.class, StrUtil.EMPTY);
    }

    /**
     * <p>
     * 设置应用加密密钥
     * </p>
     *
     * @param val 加密密钥
     */
    public static void setAppKeyEncKey(String val) {
        set(SecurityConstant.APP_KEY_ENC_KEY, val);
    }

    /**
     * <p>
     * 获取token
     * </p>
     *
     * @return /
     */
    public static String getToken() {
        return get(SecurityConstant.HEADER_AUTH, String.class);
    }

    /**
     * <p>
     * 设置token
     * </p>
     *
     * @param val /
     */
    public static void setToken(String val) {
        set(SecurityConstant.HEADER_AUTH, val);
    }


    /**
     * <p>
     * 获取用户id
     * </p>
     *
     * @return /
     */
    public static Long getUserId() {
        return get(SecurityConstant.JWT_KEY_USER_ID, Long.class);
    }

    /**
     * <p>
     * 设置用户id
     * </p>
     *
     * @param val 用户id
     */
    public static void setUserId(Long val) {
        set(SecurityConstant.JWT_KEY_USER_ID, val);
    }

    /**
     * <p>
     * 获取用户名
     * </p>
     *
     * @return /
     */
    public static String getUsername() {
        return get(SecurityConstant.JWT_KEY_USERNAME, String.class);
    }

    /**
     * <p>
     * 设置用户名
     * </p>
     *
     * @param val 用户名
     */
    public static void setUsername(String val) {
        set(SecurityConstant.JWT_KEY_USERNAME, val);
    }

    /**
     * <p>
     * 获取用户权限
     * </p>
     *
     * @return /
     */
    public static Set<String> getPermissions() {
        return get(SecurityConstant.JWT_KEY_PERMISSION, Set.class);
    }

    /**
     * <p>
     * 设置用户权限
     * </p>
     *
     * @param val 权限
     */
    public static void setPermissions(Set<String> val) {
        set(SecurityConstant.JWT_KEY_PERMISSION, val);
    }


    /**
     * <p>
     * 获取用户数据范围部门id
     * </p>
     *
     * @return /
     */
    public static Set<Long> getDataScopeDeptIds() {
        return get(SecurityConstant.JWT_KEY_DATA_SCOPE_DEPT_IDS, Set.class);
    }

    /**
     * <p>
     * 设置用户数据范围部门id
     * </p>
     *
     * @param val 部门id
     */
    public static void setDataScopeDeptIds(Set<Long> val) {
        set(SecurityConstant.JWT_KEY_DATA_SCOPE_DEPT_IDS, val);
    }

    /**
     * <p>
     * 设置用户数据范围是否全部
     * </p>
     *
     * @return /
     */
    public static boolean getDataScopeIsAll() {
        return get(SecurityConstant.JWT_KEY_DATA_SCOPE_IS_ALL, Boolean.class, false);
    }

    /**
     * <p>
     * 设置用户数据范围是否全部
     * </p>
     *
     * @param val 数据范围是否全部
     */
    public static void setDataScopeIsAll(boolean val) {
        set(SecurityConstant.JWT_KEY_DATA_SCOPE_IS_ALL, val);
    }

    /**
     * <p>
     * 设置用户数据范围是否本人
     * </p>
     *
     * @return /
     */
    public static boolean getDataScopeIsSelf() {
        return get(SecurityConstant.JWT_KEY_DATA_SCOPE_IS_SELF, Boolean.class, false);
    }

    /**
     * <p>
     * 设置用户数据范围是否本人
     * </p>
     *
     * @param val 数据范围是否本人
     */
    public static void setDataScopeIsSelf(boolean val) {
        set(SecurityConstant.JWT_KEY_DATA_SCOPE_IS_SELF, val);
    }

    /**
     * <p>
     * 获取用户数据范围用户id
     * </p>
     *
     * @return /
     */
    public static Long getDataScopeUserId() {
        return get(SecurityConstant.JWT_KEY_DATA_SCOPE_USERID, Long.class);
    }

    /**
     * <p>
     * 设置用户数据范围用户id
     * </p>
     *
     * @param val 用户数据范围用户id
     */
    public static void setDataScopeUserId(Long val) {
        set(SecurityConstant.JWT_KEY_DATA_SCOPE_USERID, val);
    }

    /**
     * <p>
     * 获取用户数据范围用户名
     * </p>
     *
     * @return /
     */
    public static String getDataScopeUserName() {
        return get(SecurityConstant.JWT_KEY_DATA_SCOPE_USERNAME, String.class);
    }

    /**
     * <p>
     * 设置用户数据范围用户名
     * </p>
     *
     * @param val 用户数据范围用户名
     */
    public static void setDataScopeUserName(String val) {
        set(SecurityConstant.JWT_KEY_DATA_SCOPE_USERNAME, val);
    }


    /**
     * <p>
     * 获取是否保存了日志
     * </p>
     *
     * @return /
     */
    public static boolean getLogSaved() {
        //全局异常时不
        return get(LogConstant.LOG_SAVED, Boolean.class, false);
    }

    /**
     * <p>
     * 设置是否保存了日志
     * </p>
     *
     * @param val 是否保存了日志
     */
    public static void setLogSaved(boolean val) {
        set(LogConstant.LOG_SAVED, val);
    }

    public static void putAll(Map<String, String> map) {
        map.forEach((k, v) -> {
            set(k, v);
        });
    }

    public static void set(String key, Object value) {
        Map<String, String> map = getLocalMap();
        map.put(key, value == null ? StrUtil.EMPTY : value.toString());
    }

    public static <T> T get(String key, Class<T> type) {
        Map<String, String> map = getLocalMap();
        return Convert.convert(type, map.get(key));
    }

    public static <T> T get(String key, Class<T> type, Object def) {
        Map<String, String> map = getLocalMap();
        return Convert.convert(type, map.getOrDefault(key, String.valueOf(def == null ? StrUtil.EMPTY : def)));
    }

    public static String get(String key) {
        Map<String, String> map = getLocalMap();
        return map.getOrDefault(key, StrUtil.EMPTY);
    }

    public static Map<String, String> getLocalMap() {
        Map<String, String> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<>(10);
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    public static void setLocalMap(Map<String, String> localMap) {
        THREAD_LOCAL.set(localMap);
    }

    private static boolean isEmptyLong(String key) {
        String val = getLocalMap().get(key);
        return val == null || StrUtil.NULL.equals(val) || StrUtil.ZERO.equals(val);
    }

    private static boolean isEmptyStr(String key) {
        String val = getLocalMap().get(key);
        return val == null || StrUtil.NULL.equals(val);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

}
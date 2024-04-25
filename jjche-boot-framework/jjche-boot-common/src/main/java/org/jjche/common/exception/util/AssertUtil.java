package org.jjche.common.exception.util;

import cn.hutool.core.lang.Assert;
import org.jjche.common.exception.enums.IBaseErrorCodeEnum;

import java.util.Map;

/**
 * <p>
 * 断言
 * </p>
 *
 * @author miaoyj
 * @since 2024-04-03
 */
public class AssertUtil {

    public AssertUtil() {
    }

    public static void isTrue(boolean expression, IBaseErrorCodeEnum codeEnum){
        Assert.isTrue(expression, () -> {
            return BusinessExceptionUtil.exception(codeEnum);
        });
    }

    public static void isTrue(boolean expression, IBaseErrorCodeEnum codeEnum, Object... params){
        Assert.isTrue(expression, () -> {
            return BusinessExceptionUtil.exception(codeEnum, params);
        });
    }

    public static void isFalse(boolean expression, IBaseErrorCodeEnum codeEnum){
        Assert.isFalse(expression, () -> {
            return BusinessExceptionUtil.exception(codeEnum);
        });
    }

    public static void isFalse(boolean expression, IBaseErrorCodeEnum codeEnum, Object... params){
        Assert.isFalse(expression, () -> {
            return BusinessExceptionUtil.exception(codeEnum, params);
        });
    }

    public static void isNull(Object object, IBaseErrorCodeEnum codeEnum){
        Assert.isNull(object, () -> {
            return BusinessExceptionUtil.exception(codeEnum);
        });
    }

    public static void isNull(Object object, IBaseErrorCodeEnum codeEnum, Object... params){
        Assert.isNull(object, () -> {
            return BusinessExceptionUtil.exception(codeEnum, params);
        });
    }

    public static <T> T notNull(T object, IBaseErrorCodeEnum codeEnum){
        return Assert.notNull(object, () -> {
            return BusinessExceptionUtil.exception(codeEnum);
        });
    }

    public static <T> T notNull(T object, IBaseErrorCodeEnum codeEnum, Object... params){
        return Assert.notNull(object, () -> {
            return BusinessExceptionUtil.exception(codeEnum, params);
        });
    }

    public static <T extends CharSequence> T notEmpty(T text, IBaseErrorCodeEnum codeEnum){
        return Assert.notEmpty(text, () -> {
            return BusinessExceptionUtil.exception(codeEnum);
        });
    }

    public static <T extends CharSequence> T notEmpty(T text, IBaseErrorCodeEnum codeEnum, Object... params){
        return Assert.notEmpty(text, () -> {
            return BusinessExceptionUtil.exception(codeEnum, params);
        });
    }

    public static <T extends CharSequence> T notBlank(T text, IBaseErrorCodeEnum codeEnum){
        return Assert.notBlank(text, () -> {
            return BusinessExceptionUtil.exception(codeEnum);
        });
    }

    public static <T extends CharSequence> T notBlank(T text, IBaseErrorCodeEnum codeEnum, Object... params){
        return Assert.notBlank(text, () -> {
            return BusinessExceptionUtil.exception(codeEnum, params);
        });
    }

    public static <T> T[] notEmpty(T[] array, IBaseErrorCodeEnum codeEnum){
        return Assert.notEmpty(array, () -> {
            return BusinessExceptionUtil.exception(codeEnum);
        });
    }

    public static <T> T[] notEmpty(T[] array, IBaseErrorCodeEnum codeEnum, Object... params){
        return Assert.notEmpty(array, () -> {
            return BusinessExceptionUtil.exception(codeEnum, params);
        });
    }

    public static <E, T extends Iterable<E>> T notEmpty(T collection, IBaseErrorCodeEnum codeEnum){
        return Assert.notEmpty(collection, () -> {
            return BusinessExceptionUtil.exception(codeEnum);
        });
    }

    public static <E, T extends Iterable<E>> T notEmpty(T collection, IBaseErrorCodeEnum codeEnum, Object... params){
        return Assert.notEmpty(collection, () -> {
            return BusinessExceptionUtil.exception(codeEnum, params);
        });
    }

    public static <K, V, T extends Map<K, V>> T notEmpty(T map, IBaseErrorCodeEnum codeEnum){
        return Assert.notEmpty(map, () -> {
            return BusinessExceptionUtil.exception(codeEnum);
        });
    }

    public static <K, V, T extends Map<K, V>> T notEmpty(T map, IBaseErrorCodeEnum codeEnum, Object... params){
        return Assert.notEmpty(map, () -> {
            return BusinessExceptionUtil.exception(codeEnum, params);
        });
    }

    public static void notEquals(Object obj1, Object obj2, IBaseErrorCodeEnum codeEnum){
        Assert.notEquals(obj1, obj2, () -> {
            return BusinessExceptionUtil.exception(codeEnum);
        });
    }

    public static void notEquals(Object obj1, Object obj2, IBaseErrorCodeEnum codeEnum, Object... params){
        Assert.notEquals(obj1, obj2, () -> {
            return BusinessExceptionUtil.exception(codeEnum, params);
        });
    }

    public static void equals(Object obj1, Object obj2, IBaseErrorCodeEnum codeEnum) {
        Assert.equals(obj1, obj2, () -> {
            return BusinessExceptionUtil.exception(codeEnum);
        });
    }

    public static void equals(Object obj1, Object obj2, IBaseErrorCodeEnum codeEnum, Object... params) {
        Assert.equals(obj1, obj2, () -> {
            return BusinessExceptionUtil.exception(codeEnum, params);
        });
    }

}

package org.jjche.cache.lock.annotation;


import java.lang.annotation.*;

/**
 * <p>
 * 防止重复提交
 * </p>
 *
 * @author miaoyj
 * @since 2023-02-01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface JRepeat {

    /**
     * 超时时间
     *
     * @return
     */
    int lockTime();


    /**
     * redis 锁key的
     *
     * @return redis 锁key
     */
    String lockKey() default "";


}

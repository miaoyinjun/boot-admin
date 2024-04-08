package org.jjche.cat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>
 * Cat拦截
 * </p>
 *
 * @author miaoyj
 * @since 2022-10-17
 */
@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface CatTransaction {
}

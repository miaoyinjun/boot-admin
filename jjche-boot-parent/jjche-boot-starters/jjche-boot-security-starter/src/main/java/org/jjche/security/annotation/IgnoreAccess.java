package org.jjche.security.annotation;

import java.lang.annotation.*;

/**
 * <p>IgnoreAccess class.</p>
 *
 * @author jacky
 * 用于标记匿名访问方法
 * @version 1.0.8-SNAPSHOT
 */
@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreAccess {

}

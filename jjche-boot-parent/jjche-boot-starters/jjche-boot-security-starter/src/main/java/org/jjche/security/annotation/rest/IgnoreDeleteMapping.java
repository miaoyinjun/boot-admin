package org.jjche.security.annotation.rest;

import org.jjche.security.annotation.IgnoreAccess;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

/**
 * Annotation for mapping HTTP {@code DELETE} requests onto specific handler
 * methods.
 * 支持匿名访问  DeleteMapping
 *
 * @author liaojinlong
 * @version 1.0.8-SNAPSHOT
 */
@IgnoreAccess
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(method = RequestMethod.DELETE)
public @interface IgnoreDeleteMapping {
    @AliasFor(annotation = RequestMapping.class)
    String name() default "";

    @AliasFor(annotation = RequestMapping.class)
    String[] value() default {};

    @AliasFor(annotation = RequestMapping.class)
    String[] path() default {};

    @AliasFor(annotation = RequestMapping.class)
    String[] params() default {};

    @AliasFor(annotation = RequestMapping.class)
    String[] headers() default {};

    @AliasFor(annotation = RequestMapping.class)
    String[] consumes() default {};

    @AliasFor(annotation = RequestMapping.class)
    String[] produces() default {};

}

package org.jjche.common.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jjche.common.serializer.dict.DictDeserializer;
import org.jjche.common.serializer.dict.DictSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 字典注解
 * </p>
 *
 * @author miaoyj
 * @since 2022-09-13
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = DictSerializer.class)
@JsonDeserialize(using = DictDeserializer.class)
public @interface Dict {

    /**
     * 字典类型
     */
    String value();
}

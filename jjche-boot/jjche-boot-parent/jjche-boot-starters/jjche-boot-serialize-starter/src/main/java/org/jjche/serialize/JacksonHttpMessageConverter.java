package org.jjche.serialize;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import de.codecentric.boot.admin.server.domain.values.InstanceId;
import de.codecentric.boot.admin.server.domain.values.Registration;
import de.codecentric.boot.admin.server.utils.jackson.AdminServerModule;
import de.codecentric.boot.admin.server.utils.jackson.InstanceIdMixin;
import de.codecentric.boot.admin.server.utils.jackson.RegistrationDeserializer;
import org.jjche.common.annotation.JacksonAllowNull;
import org.jjche.common.base.IBaseEnum;
import org.jjche.common.serializer.baseenum.BaseEnumSerializer;
import org.jjche.common.util.StrUtil;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

/**
 * <p>
 * jackson转换消息
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Component
public class JacksonHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    /**
     * <p>
     * 构造函数
     * </p>
     *
     * @author miaoyj
     * @since 2020-09-09
     */
    public JacksonHttpMessageConverter() {
        ObjectMapper objectMapper = getObjectMapper();
        // 日期格式化
        objectMapper.setDateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN));
        // 时区设置
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        objectMapper.setSerializerFactory(objectMapper.getSerializerFactory()
                .withSerializerModifier(new MyBeanSerializerModifier()));

        //解决 /instances/%5Bobject%20Object%5D/actuator/metrics
        //https://github.com/codecentric/spring-boot-admin/issues/1517
        objectMapper.addMixIn(InstanceId.class, InstanceIdMixin.class);
        String[] pwdConstructor = new String[]{".*password$"};
        objectMapper.registerModule(new AdminServerModule(pwdConstructor));
        //解决domain.values.Registration cannot deserialize from Object
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Registration.class, ToStringSerializer.instance);
        simpleModule.addDeserializer(Registration.class, new RegistrationDeserializer());
        //枚举自定义出参
        JsonSerializer<IBaseEnum> serializer = new BaseEnumSerializer();
        simpleModule.addSerializer(IBaseEnum.class, serializer);
        objectMapper.registerModule(simpleModule);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * 类型只支持application/json
     * </p>
     *
     * @author miaoyj
     * @since 2020-09-09
     */
    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return CollUtil.newArrayList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * JSON输出只处理Wrapper的包装类
     * </p>
     * <p>
     * 不注释会导致springBootAdmin显示失败
     * </p>
     *
     * @author miaoyj
     * @since 2020-09-09
     */
//    @Override
//    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
//        return R.class.isAssignableFrom(clazz);
//    }

    private class MyBeanSerializerModifier extends BeanSerializerModifier {
        @Override
        public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
            for (Object beanProperty : beanProperties) {
                BeanPropertyWriter writer = (BeanPropertyWriter) beanProperty;
                /**过滤允许为null的注解*/
                if (ObjectUtil.isNull(writer.getAnnotation(JacksonAllowNull.class))) {
                    if (isArrayType(writer)) {
                        writer.assignNullSerializer(new NullArrayJsonSerializer());
                    } else if (isNumberType(writer)) {
                        writer.assignNullSerializer(new NullNumberJsonSerializer());
                    } else if (isBooleanType(writer)) {
                        writer.assignNullSerializer(new NullBooleanJsonSerializer());
                    } else if (isStringType(writer)) {
                        writer.assignNullSerializer(new NullStringJsonSerializer());
                    }
                }
                /** 防止Long精度丢失 */
//                if (isLongType(writer)) {
//                    writer.assignSerializer(new ToStringSerializer());
//                }
            }
            return beanProperties;
        }

        private boolean isArrayType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
        }

        private boolean isStringType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return CharSequence.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz);
        }

        private boolean isNumberType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return Number.class.isAssignableFrom(clazz);
        }

        private boolean isLongType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return Long.class.isAssignableFrom(clazz);
        }

        private boolean isBooleanType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return clazz.equals(Boolean.class);
        }
    }

    private class NullArrayJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
            if (o == null) {
                jsonGenerator.writeStartArray();
                jsonGenerator.writeEndArray();
            }
        }
    }

    private class NullStringJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(StrUtil.EMPTY);
        }
    }

    private class NullNumberJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeNumber(0);
        }
    }

    private class NullBooleanJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeBoolean(false);
        }
    }
}

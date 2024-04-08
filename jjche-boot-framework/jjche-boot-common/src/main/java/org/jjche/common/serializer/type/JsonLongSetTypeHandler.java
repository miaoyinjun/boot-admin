package org.jjche.common.serializer.type;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;

import java.util.Set;

/**
 * 参考 {@link com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler} 实现
 * 在我们将字符串反序列化为 Set 并且泛型为 Long 时，如果每个元素的数值太小，会被处理成 Integer 类型，导致可能存在隐性的 BUG。
 *
 * 例如说哦，SysUserDO 的 postIds 属性
 *
 * @author 芋道源码
 */
public class JsonLongSetTypeHandler extends AbstractJsonTypeHandler<Object> {

    private static final TypeReference<Set<Long>> TYPE_REFERENCE = new TypeReference<Set<Long>>(){};

    @Override
    protected Object parse(String json) {
        return JSONUtil.toBean(json, TYPE_REFERENCE, false);
    }

    @Override
    protected String toJson(Object obj) {
        return JSONUtil.toJsonStr(obj);
    }

}

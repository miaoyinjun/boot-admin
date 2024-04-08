package org.jjche.log.biz.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import lombok.RequiredArgsConstructor;
import org.jjche.common.util.ClassUtil;
import org.jjche.common.util.StrUtil;
import org.jjche.log.biz.service.IParseFunction;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 解析注解ApiProperty对象
 * </p>
 *
 * @author miaoyj
 * @since 2022-07-14
 */
@Component
@RequiredArgsConstructor
public class ApiModelParseFunction implements IParseFunction {
    public static final String API_MODEL_OBJECT = "_apiModelObj";

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean executeBefore() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String functionName() {
        return "API_MODEL";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String apply(Object object) {
        StringBuilder result = StrUtil.builder();
        if (Iterable.class.isAssignableFrom(object.getClass())) {
            List list = Convert.toList(object);
            for (Object o : list) {
                String detail = this.getFieldDetail(o);
                result.append(detail);
            }
        } else {
            result.append(this.getFieldDetail(object));
        }

        //TODO:保存添加和删除的数据为ret，保存到detail，级别为管理员
        return result.toString();
    }

    private String getFieldDetail(Object object) {
        StringBuilder result = StrUtil.builder();
        if (object != null) {
            Map<String, Object> descMap = new HashMap<>(10);
            Map<String, String> apiModelMap = ClassUtil.getApiModelPropertyValue(object.getClass());
            Map<String, Object> objectMap = Convert.convert(Map.class, object);
            for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
                //对象字段名
                String fieldName = entry.getKey();
                Object fieldValue = entry.getValue();
                fieldValue = ClassUtil.getEnumDesc(fieldValue);
                //新值
                String fieldDesc = MapUtil.getStr(apiModelMap, fieldName);
                if (StrUtil.isNotBlank(fieldDesc)) {
                    descMap.put(fieldDesc, fieldValue);
                    result.append(StrUtil.format("[{}]是'{}'，", fieldDesc, fieldValue));
                }
            }
            result = StrUtil.builder(StrUtil.removeSuffix(result, "，"));
            result.append("；");
        }
        return result.toString();
    }
}

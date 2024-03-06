package org.jjche.system.modules.dict.api;

import org.jjche.common.api.CommonDictApi;

import java.util.Collection;

/**
 * <p>
 * 字典数据 API 接口
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-01
 */
public interface DictApi extends CommonDictApi {

    /**
     * 校验字典数据们是否有效。如下情况，视为无效：
     * 1. 字典数据不存在
     * 2. 字典数据被禁用
     *
     * @param dictType 字典类型
     * @param values 字典数据值的数组
     */
    void validateDictDataList(String dictType, Collection<String> values);
}

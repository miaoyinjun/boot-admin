package org.jjche.common.api;

import org.jjche.common.dto.DictParam;

/**
 * <p>
 * 字典
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-04
 */
public interface CommonDictApi {
    /**
     * <p>
     * 获取根据字典名称和值
     * </p>
     *
     * @param name  字典名称
     * @param value 字典值
     * @return /
     */
    DictParam getDictByNameValue(String name, String value);
}

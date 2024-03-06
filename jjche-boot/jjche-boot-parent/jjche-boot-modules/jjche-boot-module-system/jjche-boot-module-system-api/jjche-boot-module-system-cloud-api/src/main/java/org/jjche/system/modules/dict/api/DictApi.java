package org.jjche.system.modules.dict.api;

import org.jjche.common.api.CommonDictApi;
import org.jjche.common.dto.DictParam;
import org.jjche.system.constant.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

/**
 * <p>
 * 字典数据 API 接口
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-01
 */
@Component
@FeignClient(contextId = ApiConstants.NAME + "-dict-api",
        name = ApiConstants.NAME,
        path = "/sys/dictDetail/",
        url = "${FEIGN_URL_SYSTEM:}"
)
public interface DictApi extends CommonDictApi {

    /**
     * 校验字典数据们是否有效。如下情况，视为无效：
     * 1. 字典数据不存在
     * 2. 字典数据被禁用
     *
     * @param dictType 字典类型
     * @param values 字典数据值的数组
     */
    @GetMapping("valid")
    void validateDictDataList(@RequestParam String dictType, @RequestParam Collection<String> values);

    /**
     * <p>
     * 获取根据字典名称和值
     * </p>
     *
     * @param name  字典名称
     * @param value 字典值
     * @return /
     */
    @GetMapping("dict-by-name-value")
    DictParam getDictByNameValue(@RequestParam("name") String name, @RequestParam("value") String value);
}

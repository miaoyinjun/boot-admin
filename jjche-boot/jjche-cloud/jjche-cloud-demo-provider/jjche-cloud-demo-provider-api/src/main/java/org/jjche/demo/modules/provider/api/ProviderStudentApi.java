package org.jjche.demo.modules.provider.api;

import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.R;
import org.jjche.demo.constant.ProviderApiVersion;
import org.jjche.demo.modules.provider.api.enums.ProviderCourseEnum;
import org.jjche.demo.modules.provider.api.vo.ProviderVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * ProviderStudentApi
 * </p>
 *
 * @author miaoyj
 * @since 2022-03-16
 */
@FeignClient(path = ProviderApiVersion.API_PATH_PREFIX_STUDENTS,
        value = ProviderApiVersion.FEIGN_NAME)
public interface ProviderStudentApi {

    @GetMapping
    R<MyPage<ProviderVO>> page(@SpringQueryMap PageParam page,
                               @RequestParam(required = false) ProviderCourseEnum course,
                               @RequestParam(required = false) String name);
}

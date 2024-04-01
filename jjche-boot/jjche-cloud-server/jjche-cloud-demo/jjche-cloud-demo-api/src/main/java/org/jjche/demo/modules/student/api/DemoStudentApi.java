package org.jjche.demo.modules.student.api;

import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.demo.constant.DemoApiConstants;
import org.jjche.demo.modules.student.api.enums.StudentCourseEnum;
import org.jjche.demo.modules.student.api.vo.StudentApiVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * DemoStudentApi
 * </p>
 *
 * @author miaoyj
 * @since 2022-03-16
 */
@FeignClient(contextId = DemoApiConstants.NAME + "-student-api",
        name = DemoApiConstants.NAME,
        path = "/demo/internal/students",
        url = "${FEIGN_URL_DEMO:}"
)
public interface DemoStudentApi {

    /**
     * <p>
     * 学生-列表
     * </p>
     *
     * @param page /
     * @param course /
     * @param name /
    * @return /
     */
    @GetMapping
    MyPage<StudentApiVO> page(@SpringQueryMap PageParam page,
                                 @RequestParam(required = false) StudentCourseEnum course,
                                 @RequestParam(required = false) String name);
}

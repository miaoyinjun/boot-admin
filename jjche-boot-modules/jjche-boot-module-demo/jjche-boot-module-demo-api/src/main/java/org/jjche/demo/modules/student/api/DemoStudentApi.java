package org.jjche.demo.modules.student.api;

import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.demo.modules.student.api.enums.StudentCourseEnum;
import org.jjche.demo.modules.student.api.vo.StudentApiVO;

/**
 * <p>
 * 对内接口feign
 * </p>
 *
 * @author miaoyj
 * @since 2022-03-16
 */

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
    MyPage<StudentApiVO> page(PageParam page, StudentCourseEnum course, String name);
}

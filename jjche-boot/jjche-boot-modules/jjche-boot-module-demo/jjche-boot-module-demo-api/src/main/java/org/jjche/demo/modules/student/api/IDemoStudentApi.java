package org.jjche.demo.modules.student.api;

import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.R;
import org.jjche.demo.modules.student.api.enums.StudentCourseEnum;
import org.jjche.demo.modules.student.api.vo.StudentApiVO;

/**
 * <p>
 * DemoStudentApi
 * </p>
 *
 * @author miaoyj
 * @since 2022-03-16
 */

public interface IDemoStudentApi {

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
    R<MyPage<StudentApiVO>> page(PageParam page, StudentCourseEnum course, String name);
}

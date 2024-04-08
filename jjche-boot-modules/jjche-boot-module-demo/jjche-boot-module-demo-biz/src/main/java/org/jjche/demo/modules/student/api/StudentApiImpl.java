package org.jjche.demo.modules.student.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.demo.modules.student.api.enums.StudentCourseEnum;
import org.jjche.demo.modules.student.api.vo.StudentApiVO;
import org.jjche.demo.modules.student.domain.StudentDO;
import org.jjche.demo.modules.student.mapstruct.StudentMapStruct;
import org.jjche.demo.modules.student.service.StudentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生远程调用实现类
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-22
 */
@Service
@RequiredArgsConstructor
public class StudentApiImpl implements DemoStudentApi {

    private final StudentService studentService;
    private final StudentMapStruct studentMapStruct;

    @Override
    public MyPage<StudentApiVO> page(PageParam page, StudentCourseEnum course, String name) {
        LambdaQueryWrapper<StudentDO> queryWrapper = Wrappers.lambdaQuery();
        if (course != null) {
            queryWrapper.eq(StudentDO::getCourse, course);
        }
        return studentMapStruct.toApiVO(this.studentService.getBaseMapper().selectPage(page, queryWrapper));
    }
}

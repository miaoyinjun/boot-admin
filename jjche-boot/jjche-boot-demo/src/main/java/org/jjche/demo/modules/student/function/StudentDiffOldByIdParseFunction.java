package org.jjche.demo.modules.student.function;

import lombok.RequiredArgsConstructor;
import org.jjche.log.context.LogRecordContext;
import org.jjche.demo.modules.student.domain.StudentDO;
import org.jjche.demo.modules.student.mapstruct.StudentMapStruct;
import org.jjche.demo.modules.student.service.StudentService;
import org.jjche.log.biz.service.IParseFunction;
import org.jjche.log.biz.service.impl.DiffParseFunction;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 组装字段修改前后值
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-29
 */
@Component
@RequiredArgsConstructor
public class StudentDiffOldByIdParseFunction implements IParseFunction {
    private final StudentService studentService;
    private final StudentMapStruct studentMapStruct;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean executeBefore() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String functionName() {
        return "STUDENT_DIFF_OLD_BY_ID";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String apply(Object idObj) {
        Object result = null;
        if (idObj instanceof List) {
            List<Long> ids = (List<Long>) idObj;
            List<StudentDO> list = this.studentService.listByIds(ids);
            result = this.studentMapStruct.toDTO(list);
        } else {
            Long id = (Long) idObj;
            StudentDO studentDO = this.studentService.getById(id);
            result = this.studentMapStruct.toDTO(studentDO);
        }
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, result);
        return null;
    }
}

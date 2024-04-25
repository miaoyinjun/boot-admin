package org.jjche.demo.modules.student.service;

import lombok.RequiredArgsConstructor;
import org.jjche.common.exception.util.AssertUtil;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.FileUtil;
import org.jjche.demo.enums.DemoErrorCodeEnum;
import org.jjche.demo.modules.student.api.enums.StudentCourseEnum;
import org.jjche.demo.modules.student.domain.StudentDO;
import org.jjche.demo.modules.student.dto.StudentDTO;
import org.jjche.demo.modules.student.dto.StudentImportDTO;
import org.jjche.demo.modules.student.dto.StudentQueryCriteriaDTO;
import org.jjche.demo.modules.student.mapper.StudentMapper;
import org.jjche.demo.modules.student.mapstruct.StudentMapStruct;
import org.jjche.demo.modules.student.vo.StudentVO;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.jjche.common.exception.util.BusinessExceptionUtil.exception;

/**
 * <p>
 * 学生 服务实现类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
 */
@Service
@RequiredArgsConstructor
public class StudentService extends MyServiceImpl<StudentMapper, StudentDO> {

    private final StudentMapStruct studentMapStruct;

    /**
     * <p>
     * 创建
     * </p>
     *
     * @param dto 创建对象
     * @return id
     */
    @Transactional(rollbackFor = Exception.class)
    public Long save(StudentDTO dto) {
        StudentDO studentDO = this.studentMapStruct.toDO(dto);
        AssertUtil.isTrue(this.save(studentDO), DemoErrorCodeEnum.SAVE_ERROR);
        return studentDO.getId();
    }

    /**
     * <p>
     * 多选删除
     * </p>
     *
     * @param ids 主键
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids) {
        AssertUtil.isTrue(this.removeBatchByIdsWithFill(new StudentDO(), ids) == ids.size(), DemoErrorCodeEnum.DELETE_ERROR);
    }

    /**
     * <p>
     * 编辑
     * </p>
     *
     * @param dto 编辑对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(StudentDTO dto) {
        StudentDO studentDO = this.studentMapStruct.toDO(dto);
        AssertUtil.isTrue(this.updateById(studentDO), DemoErrorCodeEnum.UPDATE_ERROR);
    }

    /**
     * <p>
     * 根据ID查询
     * </p>
     *
     * @param id ID
     * @return StudentVO 对象
     */
    public StudentVO getVoById(Long id) {
        StudentDO studentDO = this.getById(id);
        AssertUtil.notNull(studentDO, DemoErrorCodeEnum.RECORD_NOT_FOUND);
        return this.studentMapStruct.toVO(studentDO);
    }

    /**
     * <p>
     * 查询数据分页
     * </p>
     *
     * @param query  条件
     * @param page   分页
     * @param course a {@link StudentCourseEnum} object.
     * @return StudentVO 分页
     */
    public MyPage<StudentVO> page(PageParam page, StudentCourseEnum course, StudentQueryCriteriaDTO query) {
        return this.baseMapper.page(page, course, query);
    }

    /**
     * <p>
     * 查询所有数据不分页
     * </p>
     *
     * @param query 条件
     * @return StudentVO 列表对象
     */
    public List<StudentVO> list(StudentQueryCriteriaDTO query) {
        List<StudentDO> list = this.baseMapper.list(query);
        return studentMapStruct.toVO(list);
    }

    /**
     * <p>
     * 导出数据
     * </p>
     *
     * @param query 条件
     */
    public void download(StudentQueryCriteriaDTO query) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<StudentVO> all = this.list(query);
        for (StudentVO student : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("姓名", student.getName());
            map.put("年龄", student.getAge());
            map.put("课程", student.getCourse().getDesc());
            map.put("创建时间", student.getGmtCreate());
            map.put("创建者", student.getCreatedBy());
            list.add(map);
        }
        try {
            HttpServletResponse httpServletResponse = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            FileUtil.downloadExcel(list, httpServletResponse);
        } catch (IOException e) {
            throw exception(DemoErrorCodeEnum.FILE_EXPORT_ERROR);
        }
    }

    /**
     * <p>
     * 执行导入
     * </p>
     *
     * @param importSet 数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void importStudent(Set<?> importSet) {
        List<StudentDO> list = studentMapStruct.toDO((Set<StudentImportDTO>) importSet);
        //判断excel中是否有重复的姓名
        long distinctNum = list.stream().map(StudentDO::getName).distinct().count();
        AssertUtil.isFalse(list.size() > distinctNum, DemoErrorCodeEnum.STUDENT_NAME_REPEAT);
        this.saveBatch(list);
    }
}

package org.jjche.demo.modules.student.service;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.FileUtil;
import org.jjche.demo.modules.student.dto.StudentDTO;
import org.jjche.demo.modules.student.dto.StudentImportDTO;
import org.jjche.demo.modules.student.dto.StudentQueryCriteriaDTO;
import org.jjche.demo.modules.student.api.enums.StudentCourseEnum;
import org.jjche.demo.modules.student.vo.StudentVO;
import org.jjche.demo.modules.student.domain.StudentDO;
import org.jjche.demo.modules.student.mapper.StudentMapper;
import org.jjche.demo.modules.student.mapstruct.StudentMapStruct;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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
     * 获取列表查询语句
     * </p>
     *
     * @param query 条件
     * @return sql
     */
    private LambdaQueryWrapper queryWrapper(StudentQueryCriteriaDTO query) {
        return MybatisUtil.assemblyLambdaQueryWrapper(query, SortEnum.ID_DESC);
    }

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
        Assert.isTrue(this.save(studentDO), "保存失败");
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
        Assert.isTrue(this.removeBatchByIdsWithFill(new StudentDO(), ids) == ids.size(), "删除失败，记录不存在");
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
        Assert.isTrue(this.updateById(studentDO), "修改失败，记录不存在");
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
        Assert.notNull(studentDO, "记录不存在");
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
        LambdaQueryWrapper<StudentDO> queryWrapper = queryWrapper(query);
        if (course != null) {
            queryWrapper.eq(StudentDO::getCourse, course);
        }
        return this.baseMapper.pageQuery(page, queryWrapper);
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
        LambdaQueryWrapper queryWrapper = queryWrapper(query);
        List<StudentDO> list = this.list(queryWrapper);
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
            throw new IllegalArgumentException("文件下载失败");
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
        Assert.isFalse(list.size() > distinctNum, "姓名不能重复");
        this.saveBatch(list);
    }
}

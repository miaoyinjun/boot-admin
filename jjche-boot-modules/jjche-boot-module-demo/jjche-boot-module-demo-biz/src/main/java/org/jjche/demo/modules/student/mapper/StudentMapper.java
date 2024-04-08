package org.jjche.demo.modules.student.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.demo.modules.student.api.enums.StudentCourseEnum;
import org.jjche.demo.modules.student.dto.StudentQueryCriteriaDTO;
import org.jjche.demo.modules.student.vo.StudentVO;
import org.jjche.demo.modules.student.domain.StudentDO;
import org.jjche.mybatis.base.MyBaseMapper;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;

import java.util.List;

/**
 * <p>
 * 学生
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
 */
public interface StudentMapper extends MyBaseMapper<StudentDO> {

    /**
     * <p>
     * 获取列表查询语句
     * </p>
     *
     * @param query 条件
     * @return sql
     */
    default LambdaQueryWrapper queryWrapper(StudentQueryCriteriaDTO query) {
        return MybatisUtil.assemblyLambdaQueryWrapper(query, SortEnum.ID_DESC);
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
    default MyPage<StudentVO> page(PageParam page, StudentCourseEnum course, StudentQueryCriteriaDTO query) {
        LambdaQueryWrapper<StudentDO> queryWrapper = queryWrapper(query);
        if (course != null) {
            queryWrapper.eq(StudentDO::getCourse, course);
        }
        return this.pageQuery(page, queryWrapper);
    }

    /**
     * <p>
     * 查询所有数据不分页
     * </p>
     *
     * @param query 条件
     * @return StudentVO 列表对象
     */
    default List<StudentDO> list(StudentQueryCriteriaDTO query) {
        LambdaQueryWrapper queryWrapper = queryWrapper(query);
        return this.selectList(queryWrapper);
    }

    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @param page    分页
     * @param wrapper 自定义sql
     * @return 分页VO
     */
    MyPage<StudentVO> pageQuery(PageParam page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * <p>
     * 查询全部
     * </p>
     *
     * @param wrapper 自定义sql
     * @return DO
     */
    List<StudentVO> queryAll(@Param(Constants.WRAPPER) Wrapper wrapper);
}

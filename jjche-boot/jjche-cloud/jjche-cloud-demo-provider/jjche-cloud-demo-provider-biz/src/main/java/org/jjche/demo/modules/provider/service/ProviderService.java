package org.jjche.demo.modules.provider.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.demo.modules.provider.api.dto.ProviderQueryCriteriaDTO;
import org.jjche.demo.modules.provider.api.enums.ProviderCourseEnum;
import org.jjche.demo.modules.provider.api.vo.ProviderVO;
import org.jjche.demo.modules.provider.domain.ProviderDO;
import org.jjche.demo.modules.provider.mapper.ProviderMapper;
import org.jjche.demo.modules.provider.mapstruct.ProviderMapStruct;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;
import org.springframework.stereotype.Service;

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
public class ProviderService extends MyServiceImpl<ProviderMapper, ProviderDO> {

    private final ProviderMapStruct studentMapStruct;


    /**
     * <p>
     * 获取列表查询语句
     * </p>
     *
     * @param query 条件
     * @return sql
     */
    private LambdaQueryWrapper queryWrapper(ProviderQueryCriteriaDTO query) {
        return MybatisUtil.assemblyLambdaQueryWrapper(query, SortEnum.ID_DESC);
    }

    /**
     * <p>
     * 查询数据分页
     * </p>
     *
     * @param name   条件
     * @param page   分页
     * @param course a {@link ProviderCourseEnum} object.
     * @return ProviderVO 分页
     */
    public MyPage<ProviderVO> page(PageParam page, ProviderCourseEnum course, String name) {
        ProviderQueryCriteriaDTO query = new ProviderQueryCriteriaDTO();
        query.setName(name);
        LambdaQueryWrapper<ProviderDO> queryWrapper = queryWrapper(query);
        if (course != null) {
            queryWrapper.eq(ProviderDO::getCourse, course);
        }
        return this.baseMapper.pageQuery(page, queryWrapper);
    }
}

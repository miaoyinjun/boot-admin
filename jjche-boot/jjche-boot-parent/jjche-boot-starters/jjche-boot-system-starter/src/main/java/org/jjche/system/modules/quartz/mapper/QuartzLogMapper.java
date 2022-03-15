package org.jjche.system.modules.quartz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.jjche.mybatis.base.MyBaseMapper;
import org.jjche.mybatis.param.MyPage;
import org.jjche.mybatis.param.PageParam;
import org.jjche.system.modules.quartz.domain.QuartzLogDO;

import java.util.List;

/**
 * <p>QuartzLogMapper interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-07
 */
public interface QuartzLogMapper extends MyBaseMapper<QuartzLogDO> {
    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @param page    分页
     * @param wrapper 自定义sql
     * @return 分页VO
     */
    MyPage<QuartzLogDO> pageQuery(PageParam page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * <p>
     * 查询全部
     * </p>
     *
     * @param wrapper 自定义sql
     * @return 分页VO
     */
    List<QuartzLogDO> queryAll(@Param(Constants.WRAPPER) Wrapper wrapper);
}

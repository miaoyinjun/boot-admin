package org.jjche.system.modules.version.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.mybatis.base.MyBaseMapper;
import org.jjche.system.modules.version.domain.VersionDO;
import org.jjche.system.modules.version.vo.VersionVO;

/**
 * <p>
 * 版本
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-23
 */
public interface VersionMapper extends MyBaseMapper<VersionDO> {
    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @param page    分页
     * @param wrapper 自定义sql
     * @return 分页VO
     */
    MyPage<VersionVO> pageQuery(PageParam page, @Param(Constants.WRAPPER) Wrapper wrapper);
}

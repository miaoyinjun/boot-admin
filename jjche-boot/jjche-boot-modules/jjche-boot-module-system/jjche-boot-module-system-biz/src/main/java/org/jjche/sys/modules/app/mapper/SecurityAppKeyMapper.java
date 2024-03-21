package org.jjche.sys.modules.app.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.mybatis.base.MyBaseMapper;
import org.jjche.sys.modules.app.api.vo.SecurityAppKeyVO;
import org.jjche.sys.modules.app.domain.SecurityAppKeyDO;

/**
 * <p>
 * 应用密钥
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-05
 */
public interface SecurityAppKeyMapper extends MyBaseMapper<SecurityAppKeyDO> {

    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @param page    分页
     * @param wrapper 自定义sql
     * @return 分页VO
     */
    MyPage<SecurityAppKeyVO> pageQuery(PageParam page, @Param(Constants.WRAPPER) Wrapper wrapper);
}

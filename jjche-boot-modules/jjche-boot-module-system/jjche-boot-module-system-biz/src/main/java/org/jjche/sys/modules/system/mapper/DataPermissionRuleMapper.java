package org.jjche.sys.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.jjche.common.dto.PermissionDataRuleDTO;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.mybatis.base.MyBaseMapper;
import org.jjche.sys.modules.system.vo.DataPermissionRuleVO;
import org.jjche.sys.modules.system.domain.DataPermissionRuleDO;

import java.util.List;

/**
 * <p>
 * 数据规则
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-10-27
 */
public interface DataPermissionRuleMapper extends MyBaseMapper<DataPermissionRuleDO> {

    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @param page    分页
     * @param wrapper 自定义sql
     * @return 分页VO
     */
    MyPage<DataPermissionRuleVO> pageQuery(PageParam page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * <p>
     * 查询全部
     * </p>
     *
     * @param wrapper 自定义sql
     * @return DO
     */
    List<DataPermissionRuleDO> queryAll(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * <p>
     * 根据用户id查询
     * </p>
     *
     * @param userId 用户id
     * @return /
     */
    List<PermissionDataRuleDTO> selectByUserId(@Param("userId") Long userId);
}

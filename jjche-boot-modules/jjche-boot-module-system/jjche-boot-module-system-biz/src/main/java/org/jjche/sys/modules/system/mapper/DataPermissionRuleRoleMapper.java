package org.jjche.sys.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.mybatis.base.MyBaseMapper;
import org.jjche.sys.modules.system.vo.DataPermissionRuleRoleVO;
import org.jjche.sys.modules.system.domain.DataPermissionRuleRoleDO;

import java.util.List;

/**
 * <p>
 * 数据规则权限
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-11-01
 */
public interface DataPermissionRuleRoleMapper extends MyBaseMapper<DataPermissionRuleRoleDO> {

    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @param page    分页
     * @param wrapper 自定义sql
     * @return 分页VO
     */
    MyPage<DataPermissionRuleRoleVO> pageQuery(PageParam page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * <p>
     * 查询全部
     * </p>
     *
     * @param wrapper 自定义sql
     * @return DO
     */
    List<DataPermissionRuleRoleDO> queryAll(@Param(Constants.WRAPPER) Wrapper wrapper);
}

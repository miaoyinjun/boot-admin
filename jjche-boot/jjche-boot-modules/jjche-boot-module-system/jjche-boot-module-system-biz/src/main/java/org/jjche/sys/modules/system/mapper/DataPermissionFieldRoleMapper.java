package org.jjche.sys.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.mybatis.base.MyBaseMapper;
import org.jjche.sys.modules.system.api.vo.DataPermissionFieldRoleVO;
import org.jjche.sys.modules.system.domain.DataPermissionFieldRoleDO;

import java.util.List;

/**
 * <p>
 * 数据字段角色
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-11-04
 */
public interface DataPermissionFieldRoleMapper extends MyBaseMapper<DataPermissionFieldRoleDO> {

    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @param page    分页
     * @param wrapper 自定义sql
     * @return 分页VO
     */
    MyPage<DataPermissionFieldRoleVO> pageQuery(PageParam page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * <p>
     * 查询全部
     * </p>
     *
     * @param wrapper 自定义sql
     * @return DO
     */
    List<DataPermissionFieldRoleDO> queryAll(@Param(Constants.WRAPPER) Wrapper wrapper);
}

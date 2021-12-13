package com.boot.admin.system.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.boot.admin.common.constant.CacheKey;
import com.boot.admin.common.dto.PermissionDataRuleDTO;
import com.boot.admin.mybatis.base.service.MyServiceImpl;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.mybatis.param.SortEnum;
import com.boot.admin.mybatis.util.MybatisUtil;
import com.boot.admin.system.modules.system.api.dto.DataPermissionRuleDTO;
import com.boot.admin.system.modules.system.api.dto.DataPermissionRuleQueryCriteriaDTO;
import com.boot.admin.system.modules.system.api.dto.DataPermissionRuleRoleQueryCriteriaDTO;
import com.boot.admin.system.modules.system.api.vo.DataPermissionRuleVO;
import com.boot.admin.system.modules.system.domain.DataPermissionRuleDO;
import com.boot.admin.system.modules.system.mapper.DataPermissionRuleMapper;
import com.boot.admin.system.modules.system.mapstruct.DataPermissionRuleMapStruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 数据规则 服务实现类
 * </p>
 *
 * @author miaoyj
 * @since 2021-10-27
 * @version 1.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
public class DataPermissionRuleService extends MyServiceImpl<DataPermissionRuleMapper, DataPermissionRuleDO> {

    private final DataPermissionRuleMapStruct sysDataPermissionRuleMapStruct;
    private final DataPermissionRuleRoleService sysDataPermissionRuleRoleService;

    /**
     * <p>
     * 创建
     * </p>
     *
     * @param dto 创建对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(DataPermissionRuleDTO dto) {

        DataPermissionRuleDO sysDataPermissionRuleDO = this.sysDataPermissionRuleMapStruct.toDO(dto);
        Assert.isTrue(this.save(sysDataPermissionRuleDO), "保存失败");
    }

    /**
     * <p>
     * 多选删除
     * </p>
     *
     * @param ids 主键
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        Assert.isTrue(this.removeBatchByIdsWithFill(new DataPermissionRuleDO(), ids) == ids.size(), "删除失败，记录不存在或权限不足");
        sysDataPermissionRuleRoleService.deleteByPermissionRuleIds(ids);
        sysDataPermissionRuleRoleService.delUserCache();
    }

    /**
     * <p>
     * 编辑
     * </p>
     *
     * @param dto 编辑对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(DataPermissionRuleDTO dto) {
        DataPermissionRuleDO sysDataPermissionRuleDO = this.getById(dto.getId());
        Assert.notNull(sysDataPermissionRuleDO, "记录不存在");

        sysDataPermissionRuleDO = this.sysDataPermissionRuleMapStruct.toDO(dto);
        Assert.isTrue(this.updateById(sysDataPermissionRuleDO), "修改失败，记录不存在或权限不足");
        sysDataPermissionRuleRoleService.delUserCache();
    }

    /**
     * <p>
     * 根据ID查询
     * </p>
     *
     * @param id ID
     * @return DataPermissionRuleVO 对象
     */
    public DataPermissionRuleVO getVoById(Long id) {
        DataPermissionRuleDO sysDataPermissionRuleDO = this.getById(id);
        Assert.notNull(sysDataPermissionRuleDO, "记录不存在或权限不足");
        return this.sysDataPermissionRuleMapStruct.toVO(sysDataPermissionRuleDO);
    }

    /**
     * <p>
     * 查询数据分页
     * </p>
     *
     * @param query 条件
     * @param sort  排序
     * @param page  分页
     * @return DataPermissionRuleVO 分页
     */
    public MyPage<DataPermissionRuleVO> pageQuery(PageParam page, DataPermissionRuleQueryCriteriaDTO query) {
        LambdaQueryWrapper queryWrapper = MybatisUtil.assemblyLambdaQueryWrapper(query, SortEnum.ID_DESC);
        return this.baseMapper.pageQuery(page, queryWrapper);
    }

    /**
     * <p>
     * 根据用户id查询
     * </p>
     *
     * @param userId 用户id
     * @return /
     */
    @Cached(name = CacheKey.PERMISSION_DATA_RULE_USER_ID, key = "#userId")
    public List<PermissionDataRuleDTO> listByUserId(Long userId) {
        return this.getBaseMapper().selectByUserId(userId);
    }

    /**
     * <p>
     * 根据角色id和权限标识查询
     * </p>
     *
     * @param page  分页
     * @param query 查询条件
     * @return /
     */
    public MyPage<DataPermissionRuleVO> pageByMenuIdAndRoleId(PageParam page, DataPermissionRuleRoleQueryCriteriaDTO query) {
        DataPermissionRuleQueryCriteriaDTO newQuery = new DataPermissionRuleQueryCriteriaDTO();
        newQuery.setMenuId(query.getMenuId());
        MyPage<DataPermissionRuleVO> myPage = this.pageQuery(page, newQuery);
        if (myPage != null) {
            List<DataPermissionRuleVO> records = myPage.getRecords();
            if (CollUtil.isNotEmpty(records)) {
                List<Long> selectedIds = sysDataPermissionRuleRoleService.listPermissionRuleIdsByMenuIdAndRoleId(query);
                if (CollUtil.isNotEmpty(selectedIds)) {
                    for (DataPermissionRuleVO record : records) {
                        for (Long selectedId : selectedIds) {
                            if (record.getId().equals(selectedId)) {
                                record.setIsSelected(true);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return myPage;
    }

    /**
     * <p>
     * 根据菜单id统计
     * </p>
     *
     * @param menuId 菜单id
     * @return /
     */
    public Long countByMenuId(Long menuId) {
        LambdaQueryWrapper<DataPermissionRuleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataPermissionRuleDO::getMenuId, menuId);
        return this.count(queryWrapper);
    }
}

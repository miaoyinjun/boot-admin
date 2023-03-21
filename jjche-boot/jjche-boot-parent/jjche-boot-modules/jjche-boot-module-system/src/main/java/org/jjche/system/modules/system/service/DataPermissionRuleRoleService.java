package org.jjche.system.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.cache.service.RedisService;
import org.jjche.common.constant.CacheKey;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.system.modules.system.api.dto.DataPermissionRuleRoleDTO;
import org.jjche.system.modules.system.api.dto.DataPermissionRuleRoleQueryCriteriaDTO;
import org.jjche.system.modules.system.domain.DataPermissionRuleRoleDO;
import org.jjche.system.modules.system.mapper.DataPermissionRuleRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 数据规则权限 服务实现类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-11-01
 */
@Service
@RequiredArgsConstructor
public class DataPermissionRuleRoleService extends MyServiceImpl<DataPermissionRuleRoleMapper, DataPermissionRuleRoleDO> {

    private final RedisService redisService;

    /**
     * <p>
     * 创建
     * </p>
     *
     * @param dto 创建对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(DataPermissionRuleRoleDTO dto) {
        Long roleId = dto.getRoleId();
        Long menuId = dto.getMenuId();
        //删除
        LambdaQueryWrapper<DataPermissionRuleRoleDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(DataPermissionRuleRoleDO::getRoleId, roleId);
        queryWrapper.in(DataPermissionRuleRoleDO::getMenuId, menuId);
        this.remove(queryWrapper);

        List<Long> dataPermissionRuleIdList = dto.getDataPermissionRuleIdList();
        if (CollUtil.isNotEmpty(dataPermissionRuleIdList)) {
            List<DataPermissionRuleRoleDO> sysDataPermissionRuleRoleDOList = CollUtil.newArrayList();
            for (Long id : dataPermissionRuleIdList) {
                DataPermissionRuleRoleDO sysDataPermissionRuleRoleDO = new DataPermissionRuleRoleDO();
                sysDataPermissionRuleRoleDO.setRoleId(roleId);
                sysDataPermissionRuleRoleDO.setMenuId(menuId);
                sysDataPermissionRuleRoleDO.setDataPermissionRuleId(id);
                sysDataPermissionRuleRoleDOList.add(sysDataPermissionRuleRoleDO);
            }
            Assert.isTrue(this.saveBatch(sysDataPermissionRuleRoleDOList), "保存失败");
        }
        this.delUserCache();
    }

    /**
     * <p>
     * 多选删除
     * </p>
     *
     * @param permissionRuleIds 数据规则id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByPermissionRuleIds(Set<Long> permissionRuleIds) {
        LambdaQueryWrapper<DataPermissionRuleRoleDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(DataPermissionRuleRoleDO::getDataPermissionRuleId, permissionRuleIds);
        this.remove(queryWrapper);
    }

    /**
     * <p>
     * 根据菜单id，角色id查询数据规则id
     * </p>
     *
     * @param query 查询
     * @return /
     */
    public List<Long> listPermissionRuleIdsByMenuIdAndRoleId(DataPermissionRuleRoleQueryCriteriaDTO query) {
        LambdaQueryWrapper<DataPermissionRuleRoleDO> queryWrapper = MybatisUtil.assemblyLambdaQueryWrapper(query);
        queryWrapper.select(DataPermissionRuleRoleDO::getDataPermissionRuleId);
        return Convert.toList(Long.class, this.listObjs(queryWrapper));
    }

    /**
     * <p>
     * 删除用户数据规则缓存
     * </p>
     */
    public void delUserCache() {
        redisService.delByKeyPrefix(CacheKey.PERMISSION_DATA_RULE_USER_ID);
    }
}

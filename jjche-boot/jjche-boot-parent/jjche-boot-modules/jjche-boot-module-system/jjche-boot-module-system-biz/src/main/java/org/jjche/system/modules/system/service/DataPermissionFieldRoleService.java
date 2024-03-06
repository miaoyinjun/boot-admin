package org.jjche.system.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.cache.service.RedisService;
import org.jjche.common.constant.PermissionDataCacheKey;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.system.modules.system.api.dto.DataPermissionFieldQueryCriteriaDTO;
import org.jjche.system.modules.system.api.dto.DataPermissionFieldRoleDTO;
import org.jjche.system.modules.system.api.dto.DataPermissionFieldRoleQueryCriteriaDTO;
import org.jjche.system.modules.system.api.dto.DataPermissionFiledRoleSelectedDTO;
import org.jjche.system.modules.system.api.vo.DataPermissionFieldRoleVO;
import org.jjche.system.modules.system.domain.DataPermissionFieldDO;
import org.jjche.system.modules.system.domain.DataPermissionFieldRoleDO;
import org.jjche.system.modules.system.mapper.DataPermissionFieldRoleMapper;
import org.jjche.system.modules.system.mapstruct.DataPermissionFieldRoleMapStruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 数据字段角色 服务实现类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-11-04
 */
@Service
@RequiredArgsConstructor
public class DataPermissionFieldRoleService extends MyServiceImpl<DataPermissionFieldRoleMapper, DataPermissionFieldRoleDO> {

    private final DataPermissionFieldRoleMapStruct sysDataPermissionFieldRoleMapStruct;
    private final RedisService redisService;

    /**
     * <p>
     * 创建
     * </p>
     *
     * @param dto 创建对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(DataPermissionFieldRoleDTO dto) {
        Long roleId = dto.getRoleId();
        Long menuId = dto.getMenuId();
        //删除
        LambdaQueryWrapper<DataPermissionFieldRoleDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(DataPermissionFieldRoleDO::getRoleId, roleId);
        queryWrapper.in(DataPermissionFieldRoleDO::getMenuId, menuId);
        this.remove(queryWrapper);

        List<DataPermissionFiledRoleSelectedDTO> dataPermissionFieldSelectedList = dto.getDataPermissionFieldSelectedList();
        if (CollUtil.isNotEmpty(dataPermissionFieldSelectedList)) {
            List<DataPermissionFieldRoleDO> sysDataPermissionFieldRoleDOList = CollUtil.newArrayList();
            for (DataPermissionFiledRoleSelectedDTO selectedDTO : dataPermissionFieldSelectedList) {
                DataPermissionFieldRoleDO sysDataPermissionFieldRoleDO = new DataPermissionFieldRoleDO();
                sysDataPermissionFieldRoleDO.setRoleId(roleId);
                sysDataPermissionFieldRoleDO.setMenuId(menuId);
                sysDataPermissionFieldRoleDO.setDataPermissionFieldId(selectedDTO.getId());
                sysDataPermissionFieldRoleDO.setIsAccessible(selectedDTO.getIsAccessible());
                sysDataPermissionFieldRoleDO.setIsEditable(selectedDTO.getIsEditable());
                sysDataPermissionFieldRoleDOList.add(sysDataPermissionFieldRoleDO);
            }
            Assert.isTrue(this.saveBatch(sysDataPermissionFieldRoleDOList), "保存失败");
        }
        this.delUserCache();
    }

    /**
     * <p>
     * 查询所有数据不分页
     * </p>
     *
     * @param query 条件
     * @return DataPermissionFieldRoleVO 列表对象
     */
    public List<DataPermissionFieldRoleVO> listQueryAll(DataPermissionFieldRoleQueryCriteriaDTO query) {
        LambdaQueryWrapper queryWrapper = MybatisUtil.assemblyLambdaQueryWrapper(query, SortEnum.ID_DESC);
        ;
        List<DataPermissionFieldRoleDO> list = this.baseMapper.queryAll(queryWrapper);
        return sysDataPermissionFieldRoleMapStruct.toVO(list);
    }

    /**
     * <p>
     * 多选删除
     * </p>
     *
     * @param permissionRuleIds 数据规则id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByPermissionFieldIds(Set<Long> permissionRuleIds) {
        LambdaQueryWrapper<DataPermissionFieldRoleDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(DataPermissionFieldRoleDO::getDataPermissionFieldId, permissionRuleIds);
        this.remove(queryWrapper);
    }

    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @param page  分页
     * @param query 条件
     * @return 分页VO
     */
    public MyPage<DataPermissionFieldRoleVO> page(PageParam page, DataPermissionFieldRoleQueryCriteriaDTO query) {
        DataPermissionFieldService dataPermissionFieldService = SpringUtil.getBean(DataPermissionFieldService.class);
        MyPage<DataPermissionFieldRoleVO> resultPage = new MyPage<>();
        List<DataPermissionFieldRoleVO> resultList = new ArrayList<>();

        //查询字段表
        DataPermissionFieldQueryCriteriaDTO fileQueryCriteria = new DataPermissionFieldQueryCriteriaDTO();
        fileQueryCriteria.setMenuId(query.getMenuId());
        LambdaQueryWrapper fileRolequeryWrapper = MybatisUtil.assemblyLambdaQueryWrapper(fileQueryCriteria, SortEnum.ID_DESC);
        MyPage<DataPermissionFieldDO> filedPage = dataPermissionFieldService.page(page, fileRolequeryWrapper);
        if (filedPage != null) {
            List<DataPermissionFieldDO> filedList = filedPage.getRecords();
            if (CollUtil.isNotEmpty(filedList)) {
                List<DataPermissionFieldRoleVO> list = this.listQueryAll(query);

                for (DataPermissionFieldDO fieldDO : filedList) {
                    Long fieldId = fieldDO.getId();
                    String fieldName = fieldDO.getName();
                    Boolean isActivated = fieldDO.getIsActivated();

                    DataPermissionFieldRoleVO fieldRoleVO = new DataPermissionFieldRoleVO();
                    fieldRoleVO.setId(fieldId);
                    fieldRoleVO.setName(fieldName);
                    fieldRoleVO.setIsActivated(isActivated);

                    for (DataPermissionFieldRoleVO fieldRole : list) {
                        if (fieldId.equals(fieldRole.getDataPermissionFieldId())) {
                            fieldRoleVO.setIsAccessible(fieldRole.getIsAccessible());
                            fieldRoleVO.setIsEditable(fieldRole.getIsEditable());
                            fieldRoleVO.setIsSelected(true);
                            break;
                        }
                    }
                    resultList.add(fieldRoleVO);
                }
            }
        }
        resultPage.setNewRecords(resultList);
        resultPage.setPages(filedPage.getPages());
        resultPage.setTotal(filedPage.getTotal());
        return resultPage;
    }


    /**
     * <p>
     * 删除用户数据字段缓存
     * </p>
     */
    public void delUserCache() {
        redisService.delByKeyPrefix(PermissionDataCacheKey.PERMISSION_DATA_FIELD_USER_ID);
    }

}

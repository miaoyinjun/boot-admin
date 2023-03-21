package org.jjche.system.modules.bpm.service.definition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.group.BpmUserGroupCreateReqVO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.group.BpmUserGroupPageReqVO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.group.BpmUserGroupUpdateReqVO;
import org.jjche.system.modules.bpm.convert.definition.BpmUserGroupConvert;
import org.jjche.system.modules.bpm.dal.dataobject.definition.BpmUserGroupDO;
import org.jjche.system.modules.bpm.mapper.BpmUserGroupMapper;
import org.jjche.common.enums.CommonStatusEnum;
import org.jjche.common.exception.BusinessException;
import org.jjche.common.param.MyPage;
import org.jjche.common.util.StrUtil;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户组 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class BpmUserGroupServiceImpl implements BpmUserGroupService {

    @Resource
    private BpmUserGroupMapper userGroupMapper;

    @Override
    public Long createUserGroup(BpmUserGroupCreateReqVO createReqVO) {
        // 插入
        BpmUserGroupDO userGroup = BpmUserGroupConvert.INSTANCE.convert(createReqVO);
        userGroupMapper.insert(userGroup);
        // 返回
        return userGroup.getId();
    }

    @Override
    public void updateUserGroup(BpmUserGroupUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateUserGroupExists(updateReqVO.getId());
        // 更新
        BpmUserGroupDO updateObj = BpmUserGroupConvert.INSTANCE.convert(updateReqVO);
        userGroupMapper.updateById(updateObj);
    }

    @Override
    public void deleteUserGroup(Long id) {
        // 校验存在
        this.validateUserGroupExists(id);
        // 删除
        userGroupMapper.deleteById(id);
    }

    private void validateUserGroupExists(Long id) {
        if (userGroupMapper.selectById(id) == null) {
            throw new BusinessException("用户组不存在");
        }
    }

    @Override
    public BpmUserGroupDO getUserGroup(Long id) {
        return userGroupMapper.selectById(id);
    }

    @Override
    public List<BpmUserGroupDO> getUserGroupList(Collection<Long> ids) {
        return userGroupMapper.selectBatchIds(ids);
    }


    @Override
    public List<BpmUserGroupDO> getUserGroupListByStatus(Integer status) {
        return userGroupMapper.selectListByStatus(status);
    }

    @Override
    public MyPage<BpmUserGroupDO> getUserGroupPage(BpmUserGroupPageReqVO pageReqVO) {
        return userGroupMapper.selectPage(pageReqVO);
    }

    @Override
    public void validUserGroups(Set<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得用户组信息
        List<BpmUserGroupDO> userGroups = userGroupMapper.selectBatchIds(ids);

        Map<Long, BpmUserGroupDO> userGroupMap = MapUtil.newHashMap();
        userGroupMap = CollUtil.toMap(userGroups, userGroupMap, BpmUserGroupDO::getId);

        // 校验
        Map<Long, BpmUserGroupDO> finalUserGroupMap = userGroupMap;
        ids.forEach(id -> {
            BpmUserGroupDO userGroup = finalUserGroupMap.get(id);
            if (userGroup == null) {
                throw new BusinessException("用户组不存在");
            }
            if (!CommonStatusEnum.ENABLE.getCode().equals(userGroup.getStatus())) {
                String msg = StrUtil.format("名字为【{}】的用户组已被禁用", userGroup.getName());
                throw new BusinessException(msg);
            }
        });
    }

}

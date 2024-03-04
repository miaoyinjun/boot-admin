package org.jjche.bpm.modules.group.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.bpm.modules.group.api.vo.BpmUserGroupCreateReqVO;
import org.jjche.bpm.modules.group.api.vo.BpmUserGroupQueryDTO;
import org.jjche.bpm.modules.group.api.vo.BpmUserGroupUpdateReqVO;
import org.jjche.bpm.modules.group.domain.BpmUserGroupDO;
import org.jjche.bpm.modules.group.mapper.BpmUserGroupMapper;
import org.jjche.bpm.modules.group.mapstruct.BpmUserGroupConvert;
import org.jjche.common.enums.CommonStatusEnum;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.StrUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;
import org.springframework.stereotype.Service;

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
@RequiredArgsConstructor
public class BpmUserGroupService extends MyServiceImpl<BpmUserGroupMapper, BpmUserGroupDO> {
    private final BpmUserGroupConvert bpmUserGroupConvert;

    /**
     * 创建用户组
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    public void createUserGroup(BpmUserGroupCreateReqVO createReqVO) {
        // 插入
        BpmUserGroupDO userGroup = bpmUserGroupConvert.convert(createReqVO);
        this.save(userGroup);
    }

    /**
     * 更新用户组
     *
     * @param updateReqVO 更新信息
     */
    public void updateUserGroup(BpmUserGroupUpdateReqVO updateReqVO) {
        // 更新
        BpmUserGroupDO updateObj = bpmUserGroupConvert.convert(updateReqVO);
        this.updateById(updateObj);
    }

    /**
     * 删除用户组
     *
     * @param ids 编号
     */
    public void deleteUserGroup(List<Long> ids) {
        this.removeByIds(ids);
    }

    /**
     * 获得用户组
     *
     * @param id 编号
     * @return 用户组
     */
    public BpmUserGroupDO getUserGroup(Long id) {
        return this.getById(id);
    }

    /**
     * 获得用户组列表
     *
     * @param ids 编号
     * @return 用户组列表
     */
    public List<BpmUserGroupDO> getUserGroupList(Collection<Long> ids) {
        return this.listByIds(ids);
    }


    /**
     * 获得指定状态的用户组列表
     *
     * @param status 状态
     * @return 用户组列表
     */
    public List<BpmUserGroupDO> getUserGroupListByStatus(Boolean status) {
        return this.baseMapper.selectListByStatus(status);
    }

    /**
     * 获得用户组分页
     *
     * @param pageReqVO 分页查询
     * @return 用户组分页
     */
    public MyPage<BpmUserGroupDO> getUserGroupPage(PageParam page, BpmUserGroupQueryDTO pageReqVO) {
        LambdaQueryWrapper queryWrapper = MybatisUtil.assemblyLambdaQueryWrapper(pageReqVO, SortEnum.ID_DESC);
        return this.page(page, queryWrapper);
    }

    /**
     * 校验用户组们是否有效。如下情况，视为无效：
     * 1. 用户组编号不存在
     * 2. 用户组被禁用
     *
     * @param ids 用户组编号数组
     */
    public void validUserGroups(Set<Long> ids){
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得用户组信息
        List<BpmUserGroupDO> userGroups = this.listByIds(ids);

        Map<Long, BpmUserGroupDO> userGroupMap = MapUtil.newHashMap();;
        userGroupMap = CollUtil.toMap(userGroups, userGroupMap, BpmUserGroupDO::getId);

        // 校验
        Map<Long, BpmUserGroupDO> finalUserGroupMap = userGroupMap;
        ids.forEach(id -> {
            BpmUserGroupDO userGroup = finalUserGroupMap.get(id);
            Assert.notNull(userGroup, "用户组不存在");
            Assert.equals(CommonStatusEnum.ENABLE.getDesc(), userGroup.getStatus(),
                    StrUtil.format("名字为【{}】的用户组已被禁用", userGroup.getName()));
        });
    }
}

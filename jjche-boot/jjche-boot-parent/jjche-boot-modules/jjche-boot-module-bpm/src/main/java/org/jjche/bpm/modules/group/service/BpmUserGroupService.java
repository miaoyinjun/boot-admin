package org.jjche.bpm.modules.group.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.bpm.modules.group.domain.BpmUserGroupDO;
import org.jjche.bpm.modules.group.api.vo.BpmUserGroupCreateReqVO;
import org.jjche.bpm.modules.group.api.vo.BpmUserGroupQueryDTO;
import org.jjche.bpm.modules.group.api.vo.BpmUserGroupUpdateReqVO;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.bpm.modules.group.mapstruct.BpmUserGroupConvert;
import org.jjche.bpm.modules.group.mapper.BpmUserGroupMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

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
}

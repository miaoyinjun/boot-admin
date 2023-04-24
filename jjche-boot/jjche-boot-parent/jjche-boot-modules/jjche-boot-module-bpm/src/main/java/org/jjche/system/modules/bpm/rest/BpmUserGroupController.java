package org.jjche.system.modules.bpm.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.system.modules.bpm.convert.definition.BpmUserGroupConvert;
import org.jjche.system.modules.bpm.dal.dataobject.definition.BpmUserGroupDO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.group.BpmUserGroupCreateReqVO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.group.BpmUserGroupQueryDTO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.group.BpmUserGroupRespVO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.group.BpmUserGroupUpdateReqVO;
import org.jjche.system.modules.bpm.service.definition.BpmUserGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "管理后台 - 用户组")
@SysRestController
@RequestMapping("/bpm/user-group")
@RequiredArgsConstructor
public class BpmUserGroupController extends BaseController {

    private final BpmUserGroupService userGroupService;

    @PostMapping
    @ApiOperation(value = "创建用户组")
    @PreAuthorize("@el.check('bpm:user-group:create')")
    public R createUserGroup(@Valid @RequestBody BpmUserGroupCreateReqVO createReqVO) {
        userGroupService.createUserGroup(createReqVO);
        return R.ok();
    }

    @PutMapping
    @ApiOperation(value = "更新用户组")
    @PreAuthorize("@el.check('bpm:user-group:update')")
    public R<Boolean> updateUserGroup(@Valid @RequestBody BpmUserGroupUpdateReqVO updateReqVO) {
        userGroupService.updateUserGroup(updateReqVO);
        return R.ok(true);
    }

    @DeleteMapping
    @ApiOperation(value = "删除用户组")
    @PreAuthorize("@el.check('bpm:user-group:delete')")
    public R deleteUserGroup(@RequestBody List<Long> ids) {
        userGroupService.deleteUserGroup(ids);
        return R.ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获得用户组")
    @PreAuthorize("@el.check('bpm:user-group:query')")
    public R<BpmUserGroupRespVO> getUserGroup(@PathVariable Long id) {
        BpmUserGroupDO userGroup = userGroupService.getUserGroup(id);
        return R.ok(BpmUserGroupConvert.INSTANCE.convert(userGroup));
    }

    @GetMapping
    @ApiOperation(value = "获得用户组分页")
    @PreAuthorize("@el.check('bpm:user-group:query')")
    public R<MyPage<BpmUserGroupRespVO>> getUserGroupPage(PageParam page, @Valid BpmUserGroupQueryDTO pageVO) {
        MyPage<BpmUserGroupDO> pageResult = userGroupService.getUserGroupPage(page, pageVO);
        return R.ok(BpmUserGroupConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/list-all-simple")
    @ApiOperation(value = "获取用户组精简信息列表", notes = "只包含被开启的用户组，主要用于前端的下拉选项")
    public R<List<BpmUserGroupRespVO>> getSimpleUserGroups() {
        // 获用户门列表，只要开启状态的
        List<BpmUserGroupDO> list = userGroupService.getUserGroupListByStatus(true);
        // 排序后，返回给前端
        return R.ok(BpmUserGroupConvert.INSTANCE.convertList2(list));
    }

}

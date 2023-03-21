package org.jjche.system.modules.bpm.rest.admin.definition;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.group.BpmUserGroupCreateReqVO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.group.BpmUserGroupPageReqVO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.group.BpmUserGroupRespVO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.group.BpmUserGroupUpdateReqVO;
import org.jjche.system.modules.bpm.convert.definition.BpmUserGroupConvert;
import org.jjche.system.modules.bpm.dal.dataobject.definition.BpmUserGroupDO;
import org.jjche.system.modules.bpm.service.definition.BpmUserGroupService;
import org.jjche.common.enums.CommonStatusEnum;
import org.jjche.common.param.MyPage;
import org.jjche.common.wrapper.response.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "管理后台 - 用户组")
@SysRestController
@RequestMapping("/bpm/user-group")
@Validated
public class BpmUserGroupController {

    @Resource
    private BpmUserGroupService userGroupService;

    @PostMapping("/create")
    @Operation(summary = "创建用户组")
    @PreAuthorize("@el.check('bpm:user-group:create')")
    public R<Long> createUserGroup(@Valid @RequestBody BpmUserGroupCreateReqVO createReqVO) {
        return R.ok(userGroupService.createUserGroup(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户组")
    @PreAuthorize("@el.check('bpm:user-group:update')")
    public R<Boolean> updateUserGroup(@Valid @RequestBody BpmUserGroupUpdateReqVO updateReqVO) {
        userGroupService.updateUserGroup(updateReqVO);
        return R.ok(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户组")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@el.check('bpm:user-group:delete')")
    public R<Boolean> deleteUserGroup(@RequestParam("id") Long id) {
        userGroupService.deleteUserGroup(id);
        return R.ok(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得用户组")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@el.check('bpm:user-group:query')")
    public R<BpmUserGroupRespVO> getUserGroup(@RequestParam("id") Long id) {
        BpmUserGroupDO userGroup = userGroupService.getUserGroup(id);
        return R.ok(BpmUserGroupConvert.INSTANCE.convert(userGroup));
    }

    @GetMapping("/page")
    @Operation(summary = "获得用户组分页")
    @PreAuthorize("@el.check('bpm:user-group:query')")
    public R<MyPage<BpmUserGroupRespVO>> getUserGroupPage(@Valid BpmUserGroupPageReqVO pageVO) {
        MyPage<BpmUserGroupDO> pageResult = userGroupService.getUserGroupPage(pageVO);
        return R.ok(BpmUserGroupConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "获取用户组精简信息列表", description = "只包含被开启的用户组，主要用于前端的下拉选项")
    public R<List<BpmUserGroupRespVO>> getSimpleUserGroups() {
        // 获用户门列表，只要开启状态的
        List<BpmUserGroupDO> list = userGroupService.getUserGroupListByStatus(CommonStatusEnum.ENABLE.getCode());
        // 排序后，返回给前端
        return R.ok(BpmUserGroupConvert.INSTANCE.convertList2(list));
    }

}

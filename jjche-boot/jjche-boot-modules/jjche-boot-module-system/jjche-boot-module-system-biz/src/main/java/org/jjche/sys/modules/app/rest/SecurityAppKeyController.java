package org.jjche.sys.modules.app.rest;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.base.BaseDTO;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.base.BaseController;
import org.jjche.log.biz.starter.annotation.LogRecord;
import org.jjche.sys.modules.app.dto.SecurityAppKeyDTO;
import org.jjche.sys.modules.app.dto.SecurityAppKeyQueryCriteriaDTO;
import org.jjche.sys.modules.app.vo.SecurityAppKeyVO;
import org.jjche.sys.modules.app.service.SecurityAppKeyService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 应用密钥 控制器
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-05
 */
@Api(tags = "应用密钥")
@ApiSupport(order = 1, author = "miaoyj")
@RestController
@RequestMapping("security_app_keys")
@RequiredArgsConstructor
public class SecurityAppKeyController extends BaseController {

    private final SecurityAppKeyService securityAppKeyService;

    @PostMapping
    @ApiOperation(value = "应用密钥-新增")
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PreAuthorize("@el.check('securityAppKey:add')")
    @LogRecord(value = "新增", category = LogCategoryType.OPERATING, type = LogType.ADD, module = "应用密钥", bizNo = "{{#_ret.data}}")
    public R create(@Validated @Valid @RequestBody SecurityAppKeyDTO dto) {
        securityAppKeyService.save(dto);
        return R.ok();
    }

    @DeleteMapping
    @ApiOperation(value = "应用密钥-删除")
    @PreAuthorize("@el.check('securityAppKey:del')")
    @LogRecord(value = "删除", category = LogCategoryType.OPERATING, type = LogType.DELETE, module = "应用密钥", bizNo = "{{#ids}}", detail = "{API_MODEL{#_oldObj}} {SECURITY_APP_KEY_DIFF_OLD_BY_ID{#ids}}")
    public R delete(@RequestBody List<Long> ids) {
        securityAppKeyService.delete(ids);
        return R.ok();
    }

    @PutMapping
    @ApiOperation(value = "应用密钥-修改")
    @PreAuthorize("@el.check('securityAppKey:edit')")
    @LogRecord(value = "修改", category = LogCategoryType.OPERATING, type = LogType.UPDATE, module = "应用密钥", bizNo = "{{#dto.id}}", detail = "{_DIFF{#dto}} {SECURITY_APP_KEY_DIFF_OLD_BY_ID{#dto.id}}")
    public R update(@Validated(BaseDTO.Update.class) @Valid @RequestBody SecurityAppKeyDTO dto) {
        securityAppKeyService.update(dto);
        return R.ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "应用密钥-查询单个")
    @PreAuthorize("@el.check('securityAppKey:list')")
    public R<SecurityAppKeyVO> getById(@PathVariable Long id) {
        return R.ok(this.securityAppKeyService.getVoById(id));
    }

    @ApiOperation(value = "应用密钥-导出")
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("@el.check('securityAppKey:list')")
    public void download(@Validated SecurityAppKeyQueryCriteriaDTO criteria) {
        securityAppKeyService.download(criteria);
    }

    @GetMapping
    @ApiOperation(value = "应用密钥-列表")
    @PreAuthorize("@el.check('securityAppKey:list')")
    public R<MyPage<SecurityAppKeyVO>> pageQuery(PageParam page, @Validated SecurityAppKeyQueryCriteriaDTO query) {
        return R.ok(securityAppKeyService.page(page, query));
    }

}

package org.jjche.system.modules.mnt.rest;

import cn.hutool.core.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.StrUtil;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.core.util.FileUtil;
import org.jjche.log.biz.starter.annotation.LogRecord;
import org.jjche.system.modules.mnt.domain.DatabaseDO;
import org.jjche.system.modules.mnt.api.dto.DatabaseDTO;
import org.jjche.system.modules.mnt.api.dto.DatabaseQueryCriteriaDTO;
import org.jjche.system.modules.mnt.service.DatabaseService;
import org.jjche.system.modules.mnt.util.SqlUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * <p>DatabaseController class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Api(tags = "运维：数据库管理")
@RequiredArgsConstructor
@SysRestController("database")
public class DatabaseController extends BaseController {

    private final String fileSavePath = FileUtil.getTmpDirPath() + "/";
    private final DatabaseService databaseService;

    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link DatabaseQueryCriteriaDTO} object.
     * @throws java.io.IOException if any.
     */
    @ApiOperation("导出数据库数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('database:list')")
    public void download(HttpServletResponse response, DatabaseQueryCriteriaDTO criteria) throws IOException {
        databaseService.download(databaseService.queryAll(criteria), response);
    }

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link DatabaseQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link R} object.
     */
    @ApiOperation(value = "查询数据库")
    @GetMapping
    @PreAuthorize("@el.check('database:list')")
    public R<MyPage<DatabaseDTO>> query(DatabaseQueryCriteriaDTO criteria, PageParam pageable) {
        return R.ok(databaseService.queryAll(criteria, pageable));
    }

    /**
     * <p>create.</p>
     *
     * @param resources a {@link DatabaseDO} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "新增", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "数据库"
    )
    @ApiOperation(value = "新增数据库")
    @PostMapping
    @PreAuthorize("@el.check('database:add')")
    public R create(@Validated @RequestBody DatabaseDO resources) {
        databaseService.create(resources);
        return R.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link DatabaseDO} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "数据库"
    )
    @ApiOperation(value = "修改数据库")
    @PutMapping
    @PreAuthorize("@el.check('database:edit')")
    public R update(@Validated @RequestBody DatabaseDO resources) {
        databaseService.update(resources);
        return R.ok();
    }

    /**
     * <p>delete.</p>
     *
     * @param ids a {@link java.util.Set} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "删除", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "数据库"
    )
    @ApiOperation(value = "删除数据库")
    @DeleteMapping
    @PreAuthorize("@el.check('database:del')")
    public R delete(@RequestBody Set<String> ids) {
        databaseService.delete(ids);
        return R.ok();
    }

    /**
     * <p>testConnect.</p>
     *
     * @param resources a {@link DatabaseDO} object.
     * @return a {@link R} object.
     */
    @ApiOperation(value = "测试数据库链接")
    @PostMapping("/testConnect")
    @PreAuthorize("@el.check('database:testConnect')")
    public R<Boolean> testConnect(@Validated @RequestBody DatabaseDO resources) {
        return R.ok(databaseService.testConnection(resources));
    }

    /**
     * <p>upload.</p>
     *
     * @param file    a {@link org.springframework.web.multipart.MultipartFile} object.
     * @param request a {@link javax.servlet.http.HttpServletRequest} object.
     * @return a {@link R} object.
     * @throws java.lang.Exception if any.
     */
    @LogRecord(
            value = "执行SQL脚本", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "数据库"
    )
    @PostMapping(value = "/upload")
    @PreAuthorize("@el.check('database:add')")
    public R<String> upload(@RequestBody MultipartFile file, HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        DatabaseDTO database = databaseService.findById(id);
        String fileName;
        Assert.notNull(database, "DatabaseDO not exist");

        fileName = file.getOriginalFilename();
        String filePath = fileSavePath + fileName;
        FileUtil.del(filePath);
        File executeFile = new File(filePath);
        FileUtil.del(executeFile);
        file.transferTo(executeFile);
        String result = SqlUtils.executeFile(database.getJdbcUrl(), database.getUserName(), database.getPwd(), executeFile);
        Assert.isTrue(StrUtil.equals("success", result), "执行失败");
        return R.ok(result);
    }
}

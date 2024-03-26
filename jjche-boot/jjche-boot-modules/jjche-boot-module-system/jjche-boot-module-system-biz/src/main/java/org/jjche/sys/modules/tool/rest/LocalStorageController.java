package org.jjche.sys.modules.tool.rest;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.enums.FileType;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.base.BaseController;
import org.jjche.core.util.FileUtil;
import org.jjche.log.biz.starter.annotation.LogRecord;
import org.jjche.sys.modules.tool.dto.LocalStorageDTO;
import org.jjche.sys.modules.tool.dto.LocalStorageQueryCriteriaDTO;
import org.jjche.sys.modules.tool.vo.LocalStorageBaseVO;
import org.jjche.sys.modules.tool.domain.LocalStorageDO;
import org.jjche.sys.modules.tool.service.LocalStorageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * <p>LocalStorageController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-09-05
 */
@Api(tags = "工具：本地存储管理")
@RestController
@RequestMapping("localStorage")
@RequiredArgsConstructor
public class LocalStorageController extends BaseController {

    private final LocalStorageService localStorageService;

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link LocalStorageQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link R} object.
     */
    @ApiOperation("查询文件")
    @GetMapping
    @PreAuthorize("@el.check('storage:list')")
    public R<MyPage<LocalStorageDTO>> query(LocalStorageQueryCriteriaDTO criteria, PageParam pageable) {
        return R.ok(localStorageService.pageQuery(criteria, pageable));
    }

    /**
     * <p>download.</p>
     *
     * @param response a {@link HttpServletResponse} object.
     * @param criteria a {@link LocalStorageQueryCriteriaDTO} object.
     * @throws IOException if any.
     */
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('storage:list')")
    public void download(HttpServletResponse response, LocalStorageQueryCriteriaDTO criteria) throws IOException {
        localStorageService.download(localStorageService.queryAll(criteria), response);
    }

    /**
     * <p>create.</p>
     *
     * @param name a {@link String} object.
     * @param file a {@link MultipartFile} object.
     * @return a {@link R} object.
     */
    @ApiOperation("上传文件")
    @PostMapping
    @PreAuthorize("@el.check('storage:add')")
    public R<List<LocalStorageBaseVO>> create(@RequestParam String name,
                                              @RequestParam(required = false, defaultValue = "true") Boolean saveDB,
                                              @RequestPart("file") MultipartFile[] file) {
        return R.ok(localStorageService.create(name, saveDB, file));
    }

    /**
     * <p>upload.</p>
     *
     * @param file a {@link MultipartFile} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "上传图片", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "存储"
    )
    @PostMapping("/pictures")
    @ApiOperation("上传图片")
    public R<List<LocalStorageBaseVO>> upload(@RequestParam MultipartFile[] file) {
        // 判断文件是否为图片
        Assert.isTrue(ArrayUtil.isNotEmpty(file), "请选择图片");
        for (MultipartFile f : file) {
            String suffix = FileUtil.getExtensionName(f.getOriginalFilename());
            Boolean isPic = FileType.IMAGE.equals(FileUtil.getFileType(suffix));
            Assert.isTrue(isPic, "只能上传图片");
        }
        return R.ok(localStorageService.create(null, Boolean.TRUE, file));

    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link LocalStorageDO} object.
     * @return a {@link R} object.
     */
    @ApiOperation("修改文件")
    @PutMapping
    @PreAuthorize("@el.check('storage:edit')")
    public R update(@Validated @RequestBody LocalStorageDO resources) {
        localStorageService.update(resources);
        return R.ok();
    }

    /**
     * <p>delete.</p>
     *
     * @param ids an array of {@link Long} objects.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "多选删除", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "存储"
    )
    @DeleteMapping
    @ApiOperation("多选删除")
    @PreAuthorize("@el.check('storage:del')")
    public R delete(@RequestBody List<String> ids) {
        localStorageService.deleteAll(ids);
        return R.ok();
    }

    /**
     * <p>list.</p>
     *
     * @param ids a {@link Set} object.
     * @return a {@link R} object.
     */
    @PostMapping("/get_files")
    @ApiOperation("获取文件信息")
    public R<List<LocalStorageBaseVO>> list(@RequestBody Set<String> ids) {
        return R.ok(localStorageService.listBaseByEncIds(ids));
    }
}

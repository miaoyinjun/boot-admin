package org.jjche.tool.modules.tool.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.log.biz.starter.annotation.LogRecord;
import org.jjche.tool.modules.tool.domain.QiniuConfigDO;
import org.jjche.tool.modules.tool.domain.QiniuContentDO;
import org.jjche.tool.modules.tool.api.dto.QiniuQueryCriteriaDTO;
import org.jjche.tool.modules.tool.service.QiNiuService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 发送邮件
 *
 * @author 郑杰
 * @version 1.0.8-SNAPSHOT
 * @since 2018/09/28 6:55:53
 */
@RequiredArgsConstructor
@SysRestController("qiNiuContent")
@Api(tags = "工具：七牛云存储管理")
public class QiniuController extends BaseController {

    private final QiNiuService qiNiuService;

    /**
     * <p>queryConfig.</p>
     *
     * @return a {@link R} object.
     */
    @GetMapping(value = "/config")
    public R<QiniuConfigDO> queryConfig() {
        return R.ok(qiNiuService.find());
    }

    /**
     * <p>updateConfig.</p>
     *
     * @param qiniuConfig a {@link QiniuConfigDO} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "配置", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "七牛云存储"
    )
    @ApiOperation("配置七牛云存储")
    @PutMapping(value = "/config")
    public R updateConfig(@Validated @RequestBody QiniuConfigDO qiniuConfig) {
        qiNiuService.config(qiniuConfig);
        qiNiuService.update(qiniuConfig.getType());
        return R.ok();
    }

    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link QiniuQueryCriteriaDTO} object.
     * @throws java.io.IOException if any.
     */
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, QiniuQueryCriteriaDTO criteria) throws IOException {
        qiNiuService.downloadList(qiNiuService.queryAll(criteria), response);
    }

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link QiniuQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link R} object.
     */
    @ApiOperation("查询文件")
    @GetMapping
    public R<MyPage> query(QiniuQueryCriteriaDTO criteria, PageParam pageable) {
        return R.ok(qiNiuService.queryAll(criteria, pageable));
    }

    /**
     * <p>upload.</p>
     *
     * @param file a {@link org.springframework.web.multipart.MultipartFile} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "上传文件", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "七牛云存储"
    )
    @ApiOperation("上传文件")
    @PostMapping
    public R<Object> upload(@RequestParam MultipartFile file) {
        QiniuContentDO qiniuContent = qiNiuService.upload(file, qiNiuService.find());
        Map<String, Object> map = new HashMap<>(3);
        map.put("id", qiniuContent.getId());
        map.put("errno", 0);
        map.put("data", new String[]{qiniuContent.getUrl()});
        return R.ok(map);
    }

    /**
     * <p>synchronize.</p>
     *
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "同步", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "七牛云存储"
    )
    @ApiOperation("同步七牛云数据")
    @PostMapping(value = "/synchronize")
    public R synchronize() {
        qiNiuService.synchronize(qiNiuService.find());
        return R.ok();
    }

    /**
     * <p>download.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link R} object.
     */
    @ApiOperation("下载文件")
    @GetMapping(value = "/download/{id}")
    public R<Object> download(@PathVariable Long id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("url", qiNiuService.download(qiNiuService.findByContentId(id), qiNiuService.find()));
        return R.ok(map);
    }

    /**
     * <p>delete.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "删除", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "七牛云存储"
    )
    @ApiOperation("删除文件")
    @DeleteMapping(value = "/{id}")
    public R delete(@PathVariable Long id) {
        qiNiuService.delete(qiNiuService.findByContentId(id), qiNiuService.find());
        return R.ok();
    }

    /**
     * <p>deleteAll.</p>
     *
     * @param ids an array of {@link java.lang.Long} objects.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "删除多张图片", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "七牛云存储"
    )
    @ApiOperation("删除多张图片")
    @DeleteMapping
    public R deleteAll(@RequestBody Long[] ids) {
        qiNiuService.deleteAll(ids, qiNiuService.find());
        return R.ok();
    }
}

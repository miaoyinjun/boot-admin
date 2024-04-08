package org.jjche.minio.rest;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.minio.messages.Bucket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.base.BaseController;
import org.jjche.minio.util.MinioUtil;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * Minio 控制器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
 */
@Api(tags = "minio文件")
@ApiSupport(order = 1, author = "miaoyj")
@RequiredArgsConstructor
public class MinioBaseController extends BaseController {

    private final MinioUtil minioUtil;

    @ApiOperation(value = "查看存储bucket是否存在")
    @GetMapping("/buckets/{name}")
    @PreAuthorize("@el.check('admin')")
    public R<Boolean> bucketExists(@PathVariable String name) {
        return R.ok(minioUtil.bucketExists(name));
    }

    @ApiOperation(value = "创建存储bucket")
    @PostMapping("/buckets")
    @PreAuthorize("@el.check('admin')")
    public R<Boolean> makeBucket(String bucketName) {
        return R.ok(minioUtil.makeBucket(bucketName));
    }

    @ApiOperation(value = "删除存储bucket")
    @DeleteMapping("/buckets/{name}")
    @PreAuthorize("@el.check('admin')")
    public R<Boolean> removeBucket(@PathVariable String name) {
        return R.ok(minioUtil.removeBucket(name));
    }

    @ApiOperation(value = "获取全部bucket")
    @GetMapping("/buckets")
    @PreAuthorize("@el.check('admin')")
    public R<List<String>> getAllBuckets() {
        List<Bucket> allBuckets = minioUtil.getAllBuckets();
        List<String> names = allBuckets.stream().map(Bucket::name).collect(Collectors.toList());
        return R.ok(names);
    }

    @ApiOperation(value = "文件上传", notes = "可选择存储桶名称和分类名称，xx/xx_thumb.png，可访问缩略图")
    @PostMapping
    public R<String> upload(@RequestPart MultipartFile file,
                            @RequestParam(required = false) String bucketName,
                            @RequestParam(required = false) String typeName) {
        String objectName = minioUtil.upload(file, bucketName, typeName);
        if (null != objectName) {
            return R.ok(objectName);
        }
        return R.error();
    }

//    @ApiOperation(value = "图片/视频预览")
//    @GetMapping("/preview")
//    public R<String> preview(@RequestParam("fileName") String fileName) {
//        return R.ok(minioUtil.preview(fileName));
//    }

    @ApiOperation(value = "文件下载")
    @GetMapping(value = "/{date}/{name}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void download(@ApiParam(value = "如：2022-12-13/xx.png")
                         @PathVariable String date, @PathVariable String name, HttpServletResponse res) {
        String fileName = date + "/" + name;
        minioUtil.download(fileName, res);
    }

    @ApiOperation(value = "删除文件", notes = "根据文件名地址删除文件")
    @DeleteMapping
    public R<Boolean> remove(String fileName) {
        return R.ok(minioUtil.remove(fileName));
    }
}
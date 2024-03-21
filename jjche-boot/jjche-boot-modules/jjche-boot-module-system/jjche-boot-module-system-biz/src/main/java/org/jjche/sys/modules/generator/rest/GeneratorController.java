package org.jjche.sys.modules.generator.rest;

import cn.hutool.core.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.base.BaseController;
import org.jjche.core.util.SpringContextHolder;
import org.jjche.sys.modules.generator.domain.ColumnInfoDO;
import org.jjche.sys.modules.generator.service.GenConfigService;
import org.jjche.sys.modules.generator.service.GeneratorService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>GeneratorController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-02
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("generator")
@Api(tags = "系统：代码生成管理")
public class GeneratorController extends BaseController {

    private final GeneratorService generatorService;
    private final GenConfigService genConfigService;

    @ApiOperation("查询数据库数据")
    @GetMapping(value = "/tables")
    @PreAuthorize("@el.check('generator:list')")
    public R<Object> queryTables(@RequestParam(defaultValue = "") String name,
                                 PageParam page) {
        return R.ok(generatorService.getTables(name, page));
    }

    /**
     * <p>queryColumns.</p>
     *
     * @param tableName a {@link String} object.
     * @return a {@link R} object.
     */
    @ApiOperation("查询字段数据")
    @GetMapping(value = "/columns")
    @PreAuthorize("@el.check('generator:list')")
    public R<MyPage> queryColumns(@RequestParam String tableName) {
        List<ColumnInfoDO> columnInfos = generatorService.getColumns(tableName);
        MyPage myPage = new MyPage();
        myPage.setRecords(columnInfos);
        myPage.setTotal(columnInfos.size());
        return R.ok(myPage);
    }

    /**
     * <p>save.</p>
     *
     * @param columnInfos a {@link List} object.
     * @return a {@link R} object.
     */
    @ApiOperation("保存字段数据")
    @PutMapping
    @PreAuthorize("@el.check('generator:list')")
    public R save(@RequestBody List<ColumnInfoDO> columnInfos) {
        generatorService.saveColumnInfo(columnInfos);
        return R.ok();
    }

    /**
     * <p>sync.</p>
     *
     * @param tables a {@link List} object.
     * @return a {@link R} object.
     */
    @ApiOperation("同步字段数据")
    @PostMapping(value = "sync")
    @PreAuthorize("@el.check('generator:list')")
    public R sync(@RequestBody List<String> tables) {
        for (String table : tables) {
            generatorService.sync(generatorService.getColumns(table), generatorService.query(table));
        }
        return R.ok();
    }

    /**
     * <p>generator.</p>
     *
     * @param tableName a {@link String} object.
     * @param type      a {@link Integer} object.
     * @param request   a {@link HttpServletRequest} object.
     * @param response  a {@link HttpServletResponse} object.
     * @return a {@link R} object.
     */
    @ApiOperation("生成代码")
    @PostMapping(value = "/{tableName}/{type}")
    @PreAuthorize("@el.check('generator:list')")
    public R generator(@PathVariable String tableName, @PathVariable Integer type, HttpServletRequest request, HttpServletResponse response) {
        boolean isDev = SpringContextHolder.isDev();
        Assert.isFalse(!isDev && type == 0, "此环境不允许生成代码，请选择预览或者下载查看！");
        switch (type) {
            // 生成代码
            case 0:
                generatorService.generator(genConfigService.find(tableName), generatorService.getColumns(tableName));
                break;
            // 预览
            case 1:
                List<Map<String, Object>> result = generatorService.preview(genConfigService.find(tableName), generatorService.getColumns(tableName));
                return R.ok(result);
            // 打包
            case 2:
                generatorService.download(genConfigService.find(tableName), generatorService.getColumns(tableName), request, response);
                break;
            default:
                throw new IllegalArgumentException("没有这个选项");
        }
        return R.ok();
    }
}

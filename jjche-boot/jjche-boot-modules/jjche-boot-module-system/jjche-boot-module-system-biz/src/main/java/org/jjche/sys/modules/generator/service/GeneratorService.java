package org.jjche.sys.modules.generator.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ZipUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.common.exception.util.AssertUtil;
import org.jjche.common.exception.util.BusinessExceptionUtil;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.StrUtil;
import org.jjche.core.util.FileUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.sys.enums.SysErrorCodeEnum;
import org.jjche.sys.modules.generator.domain.ColumnInfoDO;
import org.jjche.sys.modules.generator.domain.GenConfigDO;
import org.jjche.sys.modules.generator.mapper.ColumnInfoMapper;
import org.jjche.sys.modules.generator.mapper.GenConfigMapper;
import org.jjche.sys.modules.generator.utils.GenUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 代码生成服务
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-19
 */
@Service
@RequiredArgsConstructor
public class GeneratorService extends MyServiceImpl<ColumnInfoMapper, ColumnInfoDO> {
    private static String apiPrefix = "/demo";
    private final GenConfigMapper genConfigMapper;

    /**
     * 查询数据库元数据
     *
     * @param name 表名
     * @param page 分页参数
     * @return /
     */
    public MyPage getTables(String name, PageParam page) {
        return this.genConfigMapper.pageTable(page, name);
    }

    /**
     * 得到数据表的元数据
     *
     * @param tableName 表名
     * @return /
     */
    public List<ColumnInfoDO> getColumns(String tableName) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("table_name", tableName);
        queryWrapper.orderByAsc("id");
        List<ColumnInfoDO> columnInfos = this.list(queryWrapper);
        if (CollUtil.isNotEmpty(columnInfos)) {
            return columnInfos;
        } else {
            columnInfos = query(tableName);
            for (ColumnInfoDO columnInfo : columnInfos) {
                columnInfo.setListShow(true);
                columnInfo.setFormShow(true);

                if ((GenUtil.PK.equalsIgnoreCase(columnInfo.getKeyType())
                        && GenUtil.EXTRA.equalsIgnoreCase(columnInfo.getExtra()))) {
                    columnInfo.setNotNull(false);
                }
                if ((CollUtil.contains(GenUtil.DEFAULT_COLUMNS, columnInfo.getColumnName()))) {
                    columnInfo.setNotNull(false);
                    columnInfo.setListShow(false);
                    columnInfo.setFormShow(false);
                }
            }
        }
        this.saveBatch(columnInfos);
        return columnInfos;
    }

    /**
     * 查询数据库的表字段数据数据
     *
     * @param tableName /
     * @return /
     */
    public List<ColumnInfoDO> query(String tableName) {
        return this.genConfigMapper.queryColumn(tableName);
    }

    /**
     * 同步表数据
     *
     * @param columnInfos    /
     * @param columnInfoList /
     */
    public void sync(List<ColumnInfoDO> columnInfos, List<ColumnInfoDO> columnInfoList) {
        // 第一种情况，数据库类字段改变或者新增字段
        for (ColumnInfoDO columnInfo : columnInfoList) {
            // 根据字段名称查找
            List<ColumnInfoDO> columns = columnInfos.stream().filter(c -> c.getColumnName().equals(columnInfo.getColumnName())).collect(Collectors.toList());
            // 如果能找到，就修改部分可能被字段
            if (CollectionUtil.isNotEmpty(columns)) {
                ColumnInfoDO column = columns.get(0);
                column.setColumnType(columnInfo.getColumnType());
                column.setExtra(columnInfo.getExtra());
                column.setKeyType(columnInfo.getKeyType());
                if (StrUtil.isBlank(column.getRemark())) {
                    column.setRemark(columnInfo.getRemark());
                }
                this.saveOrUpdate(column);
            } else {
                // 如果找不到，则保存新字段信息
                this.saveOrUpdate(columnInfo);
            }
        }
        // 第二种情况，数据库字段删除了
        for (ColumnInfoDO columnInfo : columnInfos) {
            // 根据字段名称查找
            List<ColumnInfoDO> columns = columnInfoList.stream().filter(c -> c.getColumnName().equals(columnInfo.getColumnName())).collect(Collectors.toList());
            // 如果找不到，就代表字段被删除了，则需要删除该字段
            if (CollectionUtil.isEmpty(columns)) {
                this.removeById(columnInfo.getId());
            }
        }
    }

    /**
     * 保持数据
     *
     * @param columnInfos /
     */
    public void saveColumnInfo(List<ColumnInfoDO> columnInfos) {
        this.saveOrUpdateBatch(columnInfos);
    }

    /**
     * 代码生成
     *
     * @param genConfig 配置信息
     * @param columns   字段信息
     */
    public void generator(GenConfigDO genConfig, List<ColumnInfoDO> columns) {
        AssertUtil.notNull(genConfig.getId(), SysErrorCodeEnum.GENERATOR_CODE_BEFORE_CONFIG);
        try {
            GenUtil.generatorCode(columns, genConfig, apiPrefix);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw BusinessExceptionUtil.exception(SysErrorCodeEnum.GENERATOR_CODE_ERROR);
        }
    }

    /**
     * 预览
     *
     * @param genConfig 配置信息
     * @param columns   字段信息
     * @return /
     */
    public List<Map<String, Object>> preview(GenConfigDO genConfig, List<ColumnInfoDO> columns) {
        AssertUtil.notNull(genConfig.getId(), SysErrorCodeEnum.GENERATOR_CODE_BEFORE_CONFIG);
        List<Map<String, Object>> genList = GenUtil.preview(columns, genConfig, apiPrefix);
        return genList;
    }

    /**
     * 打包下载
     *
     * @param genConfig 配置信息
     * @param columns   字段信息
     * @param request   /
     * @param response  /
     */
    public void download(GenConfigDO genConfig, List<ColumnInfoDO> columns, HttpServletRequest
            request, HttpServletResponse response) {
        AssertUtil.notNull(genConfig.getId(), SysErrorCodeEnum.GENERATOR_CODE_BEFORE_CONFIG);
        try {
            File file = new File(GenUtil.download(columns, genConfig, apiPrefix));
            String zipPath = file.getPath() + ".zip";
            ZipUtil.zip(file.getPath(), zipPath);
            FileUtil.downloadFile(request, response, new File(zipPath), true);
        } catch (IOException e) {
            throw BusinessExceptionUtil.exception(SysErrorCodeEnum.GENERATOR_CODE_ZIP_ERROR);
        }
    }
}

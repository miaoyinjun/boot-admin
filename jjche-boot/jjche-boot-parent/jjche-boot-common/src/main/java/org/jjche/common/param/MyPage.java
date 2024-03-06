package org.jjche.common.param;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.EqualsAndHashCode;
import org.jjche.common.annotation.IgnoreField;
import org.jjche.common.permission.DataPermissionFieldFilterable;
import org.jjche.common.permission.DataPermissionFieldMetaSetter;
import org.jjche.common.vo.DataPermissionFieldResultVO;

import java.util.List;
import java.util.function.Function;

/**
 * <p>
 * 分页出参
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-10-10
 */
@ApiModel("分页")
@EqualsAndHashCode(callSuper = true)
public class MyPage<T> extends Page<T> implements DataPermissionFieldFilterable<T>, DataPermissionFieldMetaSetter {

    @ApiModelProperty(value = "数据")
    protected List<T> records;
    @ApiModelProperty(value = "总条数")
    protected long total;
    @ApiModelProperty(value = "总页数")
    protected long pages;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    protected long size;
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    protected long current;
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @ApiParam(hidden = true)
    @IgnoreField
    protected List<OrderItem> orders;
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    protected boolean optimizeCountSql;
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    protected boolean searchCount;
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    protected boolean hitCount;
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    protected String countId;
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    protected Long maxLimit;

    @ApiModelProperty("字段")
    private List<DataPermissionFieldResultVO> meta;
    @JsonIgnore
    @IgnoreField
    private Iterable<T> data;

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter(Function<T, T> filterFunc) {
        if (CollUtil.isNotEmpty(super.records)) {
            for (T row : super.records) {
                filterFunc.apply(row);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<T> getData() {
        return super.records;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DataPermissionFieldResultVO> getMeta() {
        return this.meta;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMeta(List<DataPermissionFieldResultVO> dataResources) {
        this.meta = dataResources;
    }

    /**
     * <p>setNewRecords.</p>
     *
     * @param list a {@link java.util.List} object.
     */
    public void setNewRecords(List list) {
        this.setRecords(list);
    }
}

package org.jjche.common.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import org.jjche.common.annotation.IgnoreField;
import org.jjche.common.constant.PageConstant;
import org.jjche.common.param.MyPage;
import org.jjche.common.vo.DataPermissionFieldResultVO;

import java.util.List;

/**
 * <p>
 * 自定义分页入参
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-09-14
 */
@ApiModel("分页")
@EqualsAndHashCode(callSuper = true)
public class PageParam<T> extends MyPage<T> {

    @JsonIgnore
    @ApiModelProperty(value = "页码 (1..N)", required = true, example = PageConstant.DEFAULT_PAGE_INDEX)
    protected long pageIndex;
    @JsonIgnore
    @ApiModelProperty(value = "每页显示的数目（最大500）", required = true, example = PageConstant.DEFAULT_PAGE_SIZE)
    protected long pageSize;

    @ApiModelProperty(hidden = true)
    protected List<T> records;
    @ApiModelProperty(hidden = true)
    protected long total;
    @ApiModelProperty(hidden = true)
    protected long pages;
    @ApiModelProperty(value = "字段", hidden = true)
    @IgnoreField
    private List<DataPermissionFieldResultVO> meta;

    /**
     * <p>Getter for the field <code>pageIndex</code>.</p>
     *
     * @return a long.
     */
    public long getPageIndex() {
        return pageIndex;
    }

    /**
     * <p>Setter for the field <code>pageIndex</code>.</p>
     *
     * @param pageIndex a long.
     */
    public void setPageIndex(long pageIndex) {
        super.setCurrent(pageIndex);
        this.pageIndex = pageIndex;
    }

    /**
     * <p>Getter for the field <code>pageSize</code>.</p>
     *
     * @return a long.
     */
    public long getPageSize() {
        return pageSize;
    }

    /**
     * <p>Setter for the field <code>pageSize</code>.</p>
     *
     * @param pageSize a long.
     */
    public void setPageSize(long pageSize) {
        super.setSize(pageSize);
        this.pageSize = pageSize;
    }

//    public T getUserOrderColumnEnum() {
//        return userOrderColumnEnum;
//    }
//
//    public void setUserOrderColumnEnum(T userOrderColumnEnum) {
//        this.userOrderColumnEnum = userOrderColumnEnum;
//    }

//    public void setOrder(Enum order) {
//        Class<? extends Enum<?>> clazz = (Class<? extends Enum<?>>) order.getClass();
//        Map<String, Object> enumMap = EnumUtil.getNameFieldMap(clazz, "value");
//        String orderColumnName = enumMap.get(order.name()).toString();
//        OrderItem orderItem = this.getPageOrderAsc()
//                ? OrderItem.asc(orderColumnName) : OrderItem.desc(orderColumnName);
//        this.addOrder(orderItem);
//    }
}

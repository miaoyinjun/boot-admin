package org.jjche.system.modules.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jjche.mybatis.base.entity.BaseEntity;

import java.util.Objects;

/**
 * <p>
 * 数据字段表
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2020-11-17
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_data_permission_field")
public class DataPermissionFieldDO extends BaseEntity {

    private Long menuId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "标识")
    private String code;

    @ApiModelProperty(value = "排序")
    private Integer sort = 999;
    @ApiModelProperty(value = "是否有效")
    private Boolean isActivated;

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}

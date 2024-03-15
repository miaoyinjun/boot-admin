package org.jjche.system.modules.system.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.base.BaseQueryCriteriaDTO;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 数据字段查询
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2020-11-18
 */
@Data
public class DataPermissionFieldQueryCriteriaDTO extends BaseQueryCriteriaDTO {


    /**
     * 精确
     */
    @NotNull(message = "菜单ID不能为空")
    @ApiModelProperty(value = "菜单ID", required = true)
    @QueryCriteria(propName = "menu_id")
    private Long menuId;


    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @QueryCriteria(type = QueryCriteria.Type.LIKE)
    private String name;
}

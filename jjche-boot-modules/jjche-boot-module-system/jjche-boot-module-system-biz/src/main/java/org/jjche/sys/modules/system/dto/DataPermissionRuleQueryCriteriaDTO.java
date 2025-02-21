package org.jjche.sys.modules.system.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.base.BaseQueryCriteriaDTO;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 数据规则 查询
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-10-27
 */
@Data
public class DataPermissionRuleQueryCriteriaDTO extends BaseQueryCriteriaDTO {


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

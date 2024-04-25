package org.jjche.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.base.BaseQueryCriteriaDTO;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 查询ID
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-11-10
 */
@Data
public class IdQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    /**
     * 精确
     */
    @ApiModelProperty(value = "id")
    @QueryCriteria(propName = "id", type = QueryCriteria.Type.EQUAL)
    @NotNull(message = "id不能为空")
    private Long id;
}

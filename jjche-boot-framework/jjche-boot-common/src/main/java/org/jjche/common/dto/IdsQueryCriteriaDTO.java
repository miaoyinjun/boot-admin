package org.jjche.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.base.BaseQueryCriteriaDTO;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * <p>
 * 查询多个ID
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-10-22
 */
@Data
public class IdsQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    /**
     * 精确
     */
    @ApiModelProperty(value = "id")
    @QueryCriteria(propName = "id", type = QueryCriteria.Type.IN)
    @NotEmpty(message = "ids不能为空")
    private Set<Long> ids;
}

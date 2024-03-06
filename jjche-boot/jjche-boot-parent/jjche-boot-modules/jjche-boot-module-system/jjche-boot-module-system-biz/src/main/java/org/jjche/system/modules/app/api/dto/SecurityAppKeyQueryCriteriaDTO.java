package org.jjche.system.modules.app.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.base.BaseQueryCriteriaDTO;

/**
 * <p>
 * 应用密钥 查询
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-05
 */
@Data
public class SecurityAppKeyQueryCriteriaDTO extends BaseQueryCriteriaDTO {
    @ApiModelProperty(value = "是否激活")
    @QueryCriteria(propName = "enabled", type = QueryCriteria.Type.EQUAL)
    private Boolean enabled;
    @ApiModelProperty(value = "应用id")
    @QueryCriteria(propName = "app_id", type = QueryCriteria.Type.LIKE)
    private String appId;
}

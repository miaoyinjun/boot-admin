package org.jjche.sys.modules.logging.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 首页统计
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-08-20
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DashboardChartLastTenVisitVO implements Serializable {
    @ApiModelProperty("总数")
    private Integer count;
    @ApiModelProperty("登录时间")
    private String loginDate;
}

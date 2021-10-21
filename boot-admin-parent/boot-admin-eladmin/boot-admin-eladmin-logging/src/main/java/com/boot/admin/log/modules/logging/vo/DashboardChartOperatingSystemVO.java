package com.boot.admin.log.modules.logging.vo;

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
 * @since 2021-08-20
 * @version 1.0.0-SNAPSHOT
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DashboardChartOperatingSystemVO implements Serializable {
    @ApiModelProperty("总数")
    private Integer count;
    @ApiModelProperty("操作系统")
    private String os;
}

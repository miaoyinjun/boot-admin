package org.jjche.sys.modules.system.api.vo;

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
public class DashboardCountVO implements Serializable {
    @ApiModelProperty("总用户数")
    private Long totalUserCount;
    @ApiModelProperty("用户数")
    private Long todayUserCount;

    @ApiModelProperty("总页面访问PV")
    private Integer totalPv;
    @ApiModelProperty("页面访问PV")
    private Integer todayPv;

    @ApiModelProperty("总登录PV")
    private Long totalLoginPv;
    @ApiModelProperty("登录PV")
    private Long todayLoginPv;

    @ApiModelProperty("总登录IV")
    private Long totalLoginIv;
    @ApiModelProperty("登录IV")
    private Long todayLoginIv;
}

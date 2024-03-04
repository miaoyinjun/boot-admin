package org.jjche.system.modules.system.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.log.modules.logging.vo.DashboardChartBrowserVO;
import org.jjche.log.modules.logging.vo.DashboardChartLastTenVisitVO;
import org.jjche.log.modules.logging.vo.DashboardChartOperatingSystemVO;

import java.io.Serializable;
import java.util.List;

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
public class DashboardChartVO implements Serializable {
    @ApiModelProperty("近十天访问记录-总数")
    private List<DashboardChartLastTenVisitVO> lastTenVisitCount;
    @ApiModelProperty("近十天访问记录-我总数")
    private List<DashboardChartLastTenVisitVO> lastTenUserVisitCount;
    @ApiModelProperty("访问用户浏览器")
    private List<DashboardChartBrowserVO> browserCount;
    @ApiModelProperty("访问用户操作系统")
    private List<DashboardChartOperatingSystemVO> operatingSystemCount;
}

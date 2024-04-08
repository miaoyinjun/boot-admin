package org.jjche.sys.modules.logging.mapper;

import org.apache.ibatis.annotations.Param;
import org.jjche.mybatis.base.MyBaseMapper;
import org.jjche.sys.modules.logging.domain.LogDO;
import org.jjche.sys.modules.logging.vo.DashboardChartBrowserVO;
import org.jjche.sys.modules.logging.vo.DashboardChartLastTenVisitVO;
import org.jjche.sys.modules.logging.vo.DashboardChartOperatingSystemVO;

import java.util.List;

/**
 * <p>LogMapper interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-24
 */
public interface LogMapper extends MyBaseMapper<LogDO> {

    /**
     * <p>
     * 查询十天内的登录统计
     * </p>
     *
     * @param tenDays  时间
     * @param username 用户名
     * @return /
     */
    List<DashboardChartLastTenVisitVO> findLastTenDaysVisitCount(@Param("tenDays") String tenDays, @Param("username") String username);

    /**
     * <p>
     * 统计浏览器
     * </p>
     *
     * @return /
     */
    List<DashboardChartBrowserVO> findByBrowser();

    /**
     * <p>
     * 统计操作系统
     * </p>
     *
     * @return /
     */
    List<DashboardChartOperatingSystemVO> findByOs();
}

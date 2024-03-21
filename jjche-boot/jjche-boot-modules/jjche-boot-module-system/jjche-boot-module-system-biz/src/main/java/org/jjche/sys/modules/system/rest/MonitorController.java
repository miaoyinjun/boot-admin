package org.jjche.sys.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.base.BaseController;
import org.jjche.sys.modules.system.service.MonitorService;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * <p>MonitorController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2020-05-02
 */
@Api(tags = "系统：服务监控管理")
@RestController
@RequestMapping("monitor")
@RequiredArgsConstructor
public class MonitorController extends BaseController {

    private final MonitorService serverService;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * <p>query.</p>
     *
     * @return a {@link R} object.
     */
    @ApiOperation("查询服务监控")
    @GetMapping("/server")
    @PreAuthorize("@el.check('monitor:list')")
    public R<Object> query() {
        return R.ok(serverService.getServers());
    }

    /**
     * <p>getCache.</p>
     *
     * @return a {@link R} object.
     */
    @ApiOperation("查询缓存监控")
    @GetMapping("/cache")
    @PreAuthorize("@el.check('monitor:list')")
    public R<Object> getCache() {
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info());
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) connection -> connection.dbSize());

        Map<String, Object> result = new HashMap<>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StringUtils.removeStart(key, "cmdstat_"));
            data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);
        return R.ok(result);
    }
}

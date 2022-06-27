package org.jjche.system.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jjche.common.annotation.Limit;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.security.annotation.rest.AnonymousGetMapping;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>LimitController class.</p>
 *
 * @author /
 * 接口限流测试类
 * @version 1.0.8-SNAPSHOT
 */
@Api(tags = "系统：限流测试管理")
@SysRestController("limit")
public class LimitController extends BaseController {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

    /**
     * 测试限流注解，下面配置说明该接口 60秒内最多只能访问 10次，保存到redis的键名为 limit_test，
     *
     * @return a int.
     */
    @AnonymousGetMapping
    @ApiOperation("测试")
    @Limit(key = "test", period = 60, count = 10, name = "testLimit", prefix = "limit")
    public int test() {
        return ATOMIC_INTEGER.incrementAndGet();
    }
}

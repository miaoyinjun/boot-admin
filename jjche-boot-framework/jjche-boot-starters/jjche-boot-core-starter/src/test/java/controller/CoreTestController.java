package controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.log.StaticLog;
import dto.LoginDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@SpringBootApplication
@RestController
@RequestMapping(value = "core", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class CoreTestController {
    public static final String ASSERT_MSG = "合同号不能为空";
    public static final String GET_PARAM_CUSTOM_MSG_VALID_USER_NAME_MSG = "用户名不能为空";
    public static final String GET_PARAM_CUSTOM_MSG_VALID_SEX_MSG = "性别不能为空";

    public static void main(String[] args) {
        SpringApplication.run(CoreTestController.class, args);
    }

    /**
     * <p>
     * 断言测试
     * </p>
     *
     * @return 列表
     * @author miaoyj
     * @since 2020-07-09
     */
    @GetMapping("assert")
    public void testAssert() {
        Assert.isTrue(false, ASSERT_MSG);
    }

    /**
     * <p>
     * 测试，URL路径参数，默认非空消息
     * </p>
     *
     * @param name 用户名
     * @param age  性别
     * @author miaoyj
     * @since 2020-07-09
     */
    @GetMapping("getParamValid")
    public void testParamValid(@RequestParam String name,
                               @RequestParam Integer age) {
    }

    /**
     * <p>
     * 测试，URL路径参数，自定义验证消息
     * </p>
     *
     * @param name 用户名
     * @param sex  性别
     * @author miaoyj
     * @since 2020-07-09
     */
    @GetMapping("getParamCustomMsgValid")
    public void testGetParamCustomMsgValid(@NotBlank(message = GET_PARAM_CUSTOM_MSG_VALID_USER_NAME_MSG)
                                           @RequestParam(required = true) String name,
                                           @NotNull(message = GET_PARAM_CUSTOM_MSG_VALID_SEX_MSG)
                                           @RequestParam(required = true) Integer sex) {
    }

    /**
     * <p>
     * 测试，POST请求，自定义验证消息
     * </p>
     *
     * @param loginBO 登录内容
     * @author miaoyj
     * @since 2020-07-09
     */
    @PostMapping("POSTParamsValid")
    public void testParamValid(@Valid @RequestBody LoginDTO loginBO) {
    }

    /**
     * <p>
     * 全局异常
     * </p>
     *
     * @author miaoyj
     * @since 2020-07-09
     */
    @GetMapping("exception")
    public void testException() {
        String[] array = {};
        StaticLog.info(array[0]);
    }
}

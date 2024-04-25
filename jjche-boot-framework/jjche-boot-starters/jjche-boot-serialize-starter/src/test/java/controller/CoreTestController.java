package controller;

import org.jjche.common.wrapper.response.R;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.LoginVO;

@SpringBootApplication
@RestController
@RequestMapping(value = "http-convert", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class CoreTestController {
    public static void main(String[] args) {
        SpringApplication.run(CoreTestController.class, args);
    }

    /**
     * <p>
     * 测试jackson
     * </p>
     *
     * @return 登录信息
     * @author miaoyj
     * @since 2020-07-09
     */
    @GetMapping("jackson")
    public R<LoginVO> wd() {
        LoginVO loginVO = new LoginVO();
        return R.ok(loginVO);
    }
}

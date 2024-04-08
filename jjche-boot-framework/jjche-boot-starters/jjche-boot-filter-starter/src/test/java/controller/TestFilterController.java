package controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping(value = TestFilterController.apiUrl, produces = MediaType.APPLICATION_JSON_VALUE)
public class TestFilterController {
    public static final String apiUrl = "filter";

    public static void main(String[] args) {
        SpringApplication.run(TestFilterController.class, args);
    }

    /**
     * <p>
     * 测试加密
     * </p>
     *
     * @return 登录信息
     * @author miaoyj
     * @since 2020-07-09
     */
    @GetMapping("test")
    public ResponseEntity wd(@RequestParam(required = false) Integer pageIndex,
                             @RequestParam(required = false) Integer pageSize,
                             @RequestParam(required = false) String name) {
        return ResponseEntity.ok("test");
    }
}

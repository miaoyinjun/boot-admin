package controller;

import dto.LoginDTO;
import org.jjche.common.dto.JwtUserDTO;
import org.jjche.common.enums.UserTypeEnum;
import org.jjche.common.wrapper.response.R;
import org.jjche.security.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@RestController
@RequestMapping(value = SecurityController.apiUrl, produces = MediaType.APPLICATION_JSON_VALUE)
public class SecurityController {
    public static final String apiUrl = "/security";
    @Autowired
    TokenProvider tokenProvider;
    //    @Autowired
//    OnlineUserService onlineUserService;
    @Autowired(required = false)
    AuthenticationManagerBuilder authenticationManagerBuilder;

    public static void main(String[] args) {
        SpringApplication.run(SecurityController.class, args);
    }

    /**
     * <p>
     * 登录
     * </p>
     *
     * @param loginDTO 登录内容
     * @return
     * @author miaoyj
     * @since 2020-09-08
     */
    @PostMapping("/login")
    public R<String> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        //用户验证
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //生成token
        final String token = tokenProvider.createToken(authentication.getName(), UserTypeEnum.PWD);
        final JwtUserDTO jwtUserDto = (JwtUserDTO) authentication.getPrincipal();
        // 保存在线信息
//        onlineUserService.save(jwtUserDto, token, request);
        return R.ok(token);
    }

    /**
     * <p>
     * 不允许访问
     * </p>
     *
     * @return 登录信息
     * @author miaoyj
     * @since 2020-07-09
     */
    @GetMapping("/not_allow")
    public R<String> notAllow(@RequestParam(required = false) Integer pageIndex,
                              @RequestParam(required = false) Integer pageSize,
                              @RequestParam(required = false) String name) {
        return R.ok("test");
    }

    /**
     * <p>
     * 访问正常
     * </p>
     *
     * @return 登录信息
     * @author miaoyj
     * @since 2020-07-09
     */
    @GetMapping("/allow")
    public R<String> allow(@RequestParam(required = false) Integer pageIndex,
                           @RequestParam(required = false) Integer pageSize,
                           @RequestParam(required = false) String name) {
        return R.ok("test");
    }
}

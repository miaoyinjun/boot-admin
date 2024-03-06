package org.jjche.security.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.jjche.common.api.CommonAuthApi;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.core.util.SpringContextHolder;
import org.jjche.security.annotation.IgnoreAccess;
import org.jjche.security.auth.sms.SmsCodeAuthenticationProvider;
import org.jjche.security.handler.JwtAuthenticationAccessDeniedHandler;
import org.jjche.security.handler.JwtAuthenticationEntryPoint;
import org.jjche.security.property.SecurityProperties;
import org.jjche.security.property.SecurityRoleUrlProperties;
import org.jjche.security.property.SecurityUrlProperties;
import org.jjche.security.security.TokenConfigurer;
import org.jjche.security.util.RequestMethodEnum;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

/**
 * <p>SecurityConfig class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Order(2)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static String ROLE_NAME_PREFIX = "ROLE_";

    private final JwtAuthenticationEntryPoint authenticationErrorHandler;
    private final JwtAuthenticationAccessDeniedHandler jwtAccessDeniedHandler;
    private final ApplicationContext applicationContext;
    private final SecurityProperties properties;
    private final CommonAuthApi commonAuthApi;
    private final UserDetailsService userDetailsService;
    private final UserDetailsService smsUserDetailsService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // 去除 ROLE_ 前缀
        return new GrantedAuthorityDefaults("");
    }

    /**
     * <p>passwordEncoder.</p>
     *
     * @return a {@link org.springframework.security.crypto.password.PasswordEncoder} object.
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        // 密码加密方式
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        SecurityUrlProperties securityUrlProperties = properties.getUrl();
        // 搜寻匿名标记 url： @IgnoreAccess
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = ((RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping")).getHandlerMethods();
        // 获取匿名标记，同时包括了配置文件里的忽略url
        Map<String, Set<String>> ignoreUrls = getIgnoreUrl(handlerMethodMap, securityUrlProperties);
        web.ignoring()
                // 放行OPTIONS请求
                .antMatchers(HttpMethod.OPTIONS, "/**")
                // 自定义匿名访问所有url放行：允许匿名和带Token访问，细腻化到每个 Request 类型
                // GET
                .antMatchers(HttpMethod.GET, ignoreUrls.get(RequestMethodEnum.GET.getType()).toArray(new String[0]))
                // POST
                .antMatchers(HttpMethod.POST, ignoreUrls.get(RequestMethodEnum.POST.getType()).toArray(new String[0]))
                // PUT
                .antMatchers(HttpMethod.PUT, ignoreUrls.get(RequestMethodEnum.PUT.getType()).toArray(new String[0]))
                // PATCH
                .antMatchers(HttpMethod.PATCH, ignoreUrls.get(RequestMethodEnum.PATCH.getType()).toArray(new String[0]))
                // DELETE
                .antMatchers(HttpMethod.DELETE, ignoreUrls.get(RequestMethodEnum.DELETE.getType()).toArray(new String[0]))
                // 所有类型的接口都放行
                .antMatchers(ignoreUrls.get(RequestMethodEnum.ALL.getType()).toArray(new String[0]));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests = httpSecurity.authorizeRequests();
        //加载配置文件，角色对应关系
        SecurityUrlProperties securityUrlProperties = properties.getUrl();
        authorizeRequests = setPropertityUrl(authorizeRequests, securityUrlProperties);

        authorizeRequests.and()
                // 禁用 CSRF
                .csrf().disable()
                // 授权异常
                .exceptionHandling().authenticationEntryPoint(authenticationErrorHandler).accessDeniedHandler(jwtAccessDeniedHandler)
                // 防止iframe 造成跨域
                .and().headers().frameOptions().disable()
                // 不创建会话
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests();

        // cloud所有都不需要认证，不走TokenFilter
        if (SpringContextHolder.isCloud()) {
            authorizeRequests.anyRequest().permitAll();
        }// 所有请求都需要认证
        else {
            authorizeRequests.anyRequest().authenticated().and().apply(securityConfigurerAdapter());
        }
    }

    /**
     * <p>configureGlobal.</p>
     *
     * @param auth a {@link org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder} object.
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(smsCodeAuthenticationProvider());
        auth.authenticationProvider(usernamePasswordAuthenticationProvider());
        authenticationManagerBuilder.authenticationProvider(smsCodeAuthenticationProvider());
        authenticationManagerBuilder.authenticationProvider(usernamePasswordAuthenticationProvider());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * <p>
     * 短信登录
     * </p>
     *
     * @return 短信
     */
    @Bean("smsCodeAuthenticationProvider")
    public SmsCodeAuthenticationProvider smsCodeAuthenticationProvider() {
        SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();
        smsCodeAuthenticationProvider.setUserDetailsService(smsUserDetailsService);
        return smsCodeAuthenticationProvider;
    }

    /**
     * <p>
     * 密码登录
     * </p>
     *
     * @return 密码
     */
    @Bean("usernamePasswordAuthenticationProvider")
    public DaoAuthenticationProvider usernamePasswordAuthenticationProvider() {
        DaoAuthenticationProvider usernamePasswordAuthenticationProvider = new DaoAuthenticationProvider();
        usernamePasswordAuthenticationProvider.setUserDetailsService(userDetailsService);
        if (passwordEncoder() != null) {
            usernamePasswordAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        }
        usernamePasswordAuthenticationProvider.setHideUserNotFoundExceptions(false);
        return usernamePasswordAuthenticationProvider;
    }

    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry setPropertityUrl(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests, SecurityUrlProperties securityUrlProperties) {
        if (ObjectUtil.isNotNull(securityUrlProperties)) {
            List<SecurityRoleUrlProperties> roleUrlList = securityUrlProperties.getRoleUrls();
            if (CollUtil.isNotEmpty(roleUrlList)) {
                for (SecurityRoleUrlProperties roleUrl : roleUrlList) {
                    String roleName = roleUrl.getRoleName();
                    List<String> rUrlList = roleUrl.getUrls();
                    if (StrUtil.isNotBlank(roleName) && CollUtil.isNotEmpty(rUrlList)) {
                        /** hasAnyRole 删除ROLE_前缀*/
                        if (roleName.startsWith(ROLE_NAME_PREFIX)) {
                            roleName = StrUtil.removePrefix(roleName, ROLE_NAME_PREFIX);
                        }
                        for (String url : rUrlList) {
                            authorizeRequests.antMatchers(url).hasAnyRole(roleName);
                        }
                    }
                }
            }
        }
        return authorizeRequests;
    }

    private Map<String, Set<String>> getIgnoreUrl(Map<RequestMappingInfo, HandlerMethod> handlerMethodMap, SecurityUrlProperties securityUrlProperties) {
        Map<String, Set<String>> ignoreUrls = new HashMap<>(8);
        Set<String> get = new HashSet<>();
        Set<String> post = new HashSet<>();
        Set<String> put = new HashSet<>();
        Set<String> patch = new HashSet<>();
        Set<String> delete = new HashSet<>();
        Set<String> all = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            IgnoreAccess ignoreAccess = handlerMethod.getMethodAnnotation(IgnoreAccess.class);
            if (null != ignoreAccess) {
                List<RequestMethod> requestMethods = new ArrayList<>(infoEntry.getKey().getMethodsCondition().getMethods());
                RequestMethodEnum request = RequestMethodEnum.find(requestMethods.size() == 0 ? RequestMethodEnum.ALL.getType() : requestMethods.get(0).name());
                switch (Objects.requireNonNull(request)) {
                    case GET:
                        get.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        break;
                    case POST:
                        post.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        break;
                    case PUT:
                        put.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        break;
                    case PATCH:
                        patch.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        break;
                    case DELETE:
                        delete.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        break;
                    default:
                        all.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        break;
                }
            }

            /** 配置文件忽略URL*/
            List<String> excludeAllUrls = new ArrayList<>();
            List<String> excludeDefaultUrls = securityUrlProperties.getExcludeDefaultUrls();
            List<String> excludeUrls = securityUrlProperties.getExcludeUrls();
            if (CollUtil.isNotEmpty(excludeUrls)) {
                excludeAllUrls.addAll(excludeUrls);
            }
            if (CollUtil.isNotEmpty(excludeDefaultUrls)) {
                excludeAllUrls.addAll(excludeDefaultUrls);
                for (String excludeUrl : excludeAllUrls) {
                    get.add(excludeUrl);
                    post.add(excludeUrl);
                    put.add(excludeUrl);
                    patch.add(excludeUrl);
                    delete.add(excludeUrl);
                    all.add(excludeUrl);
                }
            }
        }
        ignoreUrls.put(RequestMethodEnum.GET.getType(), get);
        ignoreUrls.put(RequestMethodEnum.POST.getType(), post);
        ignoreUrls.put(RequestMethodEnum.PUT.getType(), put);
        ignoreUrls.put(RequestMethodEnum.PATCH.getType(), patch);
        ignoreUrls.put(RequestMethodEnum.DELETE.getType(), delete);
        ignoreUrls.put(RequestMethodEnum.ALL.getType(), all);

        //将忽略的url设置到公用变量中，swagger会用到
        SecurityConstant.IGNORE_URLS.addAll(get);
        SecurityConstant.IGNORE_URLS.addAll(post);
        SecurityConstant.IGNORE_URLS.addAll(put);
        SecurityConstant.IGNORE_URLS.addAll(patch);
        SecurityConstant.IGNORE_URLS.addAll(delete);
        SecurityConstant.IGNORE_URLS.addAll(all);
        return ignoreUrls;
    }

    private TokenConfigurer securityConfigurerAdapter() {
        return new TokenConfigurer(commonAuthApi);
    }

}

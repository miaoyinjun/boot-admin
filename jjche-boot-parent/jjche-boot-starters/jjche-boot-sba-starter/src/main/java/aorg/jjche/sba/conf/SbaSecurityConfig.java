package aorg.jjche.sba.conf;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.Collections;

/**
 * <p>
 * sba安全配置
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Configuration
public class SbaSecurityConfig {

    /**
     * <p>
     * test,uat,prod 环境加载
     * cloud版本全部放开
     * </p>
     */
    @Configuration
    @Order(1)
    public static class SecuritySecureConfig extends WebSecurityConfigurerAdapter {
        @Value("${jjche.sba.security.enabled:false}")
        private boolean sbaSecurityEnabled;

        private final String adminContextPath;
        @Autowired
        private SecurityProperties securityProperties;

        public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
            this.adminContextPath = adminServerProperties.getContextPath();
        }

        @Bean
        @ConditionalOnMissingBean(PasswordEncoder.class)
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            boolean isLogin = sbaSecurityEnabled;
            //单体版才支持登录BooleanUtil.isFalse(SpringContextHolder.isCloud()) &&
            if (isLogin) {
                PasswordEncoder encoder = new BCryptPasswordEncoder();
                SecurityProperties.User user = securityProperties.getUser();
                String username = user.getName();
                String password = user.getPassword();
                password = encoder.encode(password);
                auth.inMemoryAuthentication().withUser(username).password(password).roles("ADMIN");
            }
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //单体版才支持登录BooleanUtil.isFalse(SpringContextHolder.isCloud()) &&
            boolean isLogin = sbaSecurityEnabled;
            if (isLogin) {
                SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
                //登录跳转
                successHandler.setTargetUrlParameter("redirectTo");
                http.authorizeRequests().antMatchers(adminContextPath + "/assets/**").permitAll().antMatchers(adminContextPath + "/login").permitAll().and().formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler).and().logout().logoutUrl(adminContextPath + "/logout").and().httpBasic().and().rememberMe().and().csrf().disable().headers().frameOptions().disable();
                /**解决与其它安全配置冲突问题*/
                http.antMatcher(adminContextPath + "/**").authorizeRequests().anyRequest().authenticated();
            } else {
                /**解决与其它安全配置冲突问题*/
                http.antMatcher(adminContextPath + "/**").authorizeRequests().anyRequest().permitAll();
            }
            http.csrf().disable().headers().frameOptions().disable();
            //解决api-gateway访问JjcheCloudMonitor跨域
            http.cors().configurationSource(corsConfigurationSource());
        }
    }

    /**
     * <p>
     * 跨域
     * </p>
     *
     * @return /
     */
    public static CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(false);
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setMaxAge(Duration.ofHours(1));
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
/**
 * 解决 httpTrace失效
 *
 * @return a {@link org.springframework.boot.actuate.trace.http.HttpTraceRepository} object.
 * <p>
 * //    @ConditionalOnMissingBean
 * //    @Bean
 * //    public HttpTraceRepository httpTraceRepository() {
 * //        return new InMemoryHttpTraceRepository();
 * //    }
 */
}
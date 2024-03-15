package org.jjche.swagger.conf;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.swagger.property.SwaggerSecurityJwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * <p>
 * Swagger配置
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
public class SwaggerConfig implements EnvironmentAware {
    @Autowired
    private SwaggerProperties swaggerProperty;
    @Autowired
    private OpenApiExtensionResolver openApiExtensionResolver;

    /**
     * <p>notRegex.</p>
     *
     * @param pathRegex a {@link java.lang.String} object.
     * @return a {@link java.util.function.Predicate} object.
     */
    public static Predicate<String> notRegex(final String pathRegex) {
        return new Predicate<String>() {
            @Override
            public boolean test(String input) {
                return !input.matches(pathRegex);
            }
        };
    }

    /**
     * <p>getRegex.</p>
     *
     * @param pathRegex a {@link java.lang.String} object.
     * @return a {@link java.util.function.Predicate} object.
     */
    public static Predicate<String> getRegex(final String pathRegex) {
        return new Predicate<String>() {
            @Override
            public boolean test(String input) {
                return input.matches(pathRegex);
            }
        };
    }

    /**
     * <p>swaggerApi</p>
     *
     * @return a {@link springfox.documentation.spring.web.plugins.Docket} object.
     */
    @SuppressWarnings("unchecked")
    @Bean
    public Docket swaggerApi() {
        String defaultGroupName = "default";
        String ignoreFilterPath = this.ignoreFilterPath();
        List<RequestParameter> operationParameters = CollUtil.newArrayList();
        //灰度头部
        operationParameters.addAll(grayHeaders());
        ApiSelectorBuilder select = new Docket(DocumentationType.SWAGGER_2).apiInfo(backendApiInfo()).groupName(defaultGroupName).select().apis(RequestHandlerSelectors.basePackage(swaggerProperty.getBasePackage())).build().extensions(openApiExtensionResolver.buildExtensions(defaultGroupName)).securityContexts(CollectionUtil.newArrayList(securityContext())).securitySchemes(CollectionUtil.newArrayList(apiKeyAuth(), apiKeyGrayVersion())).genericModelSubstitutes(DeferredResult.class).useDefaultResponseMessages(false).forCodeGeneration(false).pathMapping("/").select();
        //过滤的接口
        select.paths(PathSelectors.regex(swaggerProperty.getFilterPath() + "/.*")).paths(notRegex(ignoreFilterPath));
        return select.build().globalRequestParameters(operationParameters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnvironment(Environment environment) {
        Contact contact = new Contact(swaggerProperty.getContactName(), swaggerProperty.getContactUrl(), swaggerProperty.getContactEmail());
        swaggerProperty.setContact(contact);
    }

    /**
     * <p>
     * 文档描述
     * </p>
     *
     * @return /
     */
    private ApiInfo backendApiInfo() {
        return new ApiInfo(swaggerProperty.getTitle(), swaggerProperty.getDescription(), swaggerProperty.getVersion(), "", swaggerProperty.getContact(), null, null, new ArrayList<VendorExtension>());
    }

    /**
     * <p>
     * 认证header-Authorize菜单
     * </p>
     *
     * @return apiKey
     */
    private ApiKey apiKeyAuth() {
        SwaggerSecurityJwtProperties swaggerPropertySecurityJwt = swaggerProperty.getSecurityJwt();
        String tokenHeader = swaggerPropertySecurityJwt.getTokenHeader();
        return new ApiKey(tokenHeader, tokenHeader, "header");
    }

    /**
     * <p>
     * 灰度发布标识-Authorize菜单
     * </p>
     *
     * @return /
     */
    private ApiKey apiKeyGrayVersion() {
        return new ApiKey(SecurityConstant.FEIGN_GRAY_TAG, SecurityConstant.FEIGN_GRAY_TAG, "header");
    }

    /**
     * <p>
     * 接口header读取apiKey，加锁标记
     * </p>
     *
     * @return /
     */
    private SecurityContext securityContext() {
        //获取Spring Security里忽略的url
        Set<String> ignoreUrls = SecurityConstant.IGNORE_URLS;
        //@ignoreGetMapping("/{id}") 启动失败问题，但无法删除锁标记
        ignoreUrls.removeIf(obj -> obj.contains("{") || obj.contains("}"));
        String notPathRegex = StrUtil.join("|", ignoreUrls);
        //匹配替换正确的格式
        notPathRegex = notPathRegex.replaceAll("\\*\\*", "*");
        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(notRegex(notPathRegex)).build();
    }

    /**
     * <p>
     * 接口header-灰度
     * </p>
     *
     * @return /
     */
    private SecurityContext grayContext() {
        //全部加入灰度标识
        return SecurityContext.builder().securityReferences(grayHeader()).build();
    }

    /**
     * <p>
     * 所有请求-头部认证必填
     * </p>
     *
     * @return /
     */
    private List<SecurityReference> defaultAuth() {
        SwaggerSecurityJwtProperties swaggerPropertySecurityJwt = swaggerProperty.getSecurityJwt();
        String tokenHeader = swaggerPropertySecurityJwt.getTokenHeader();

        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        //token
        SecurityReference token = new SecurityReference(tokenHeader, authorizationScopes);
        return CollectionUtil.newArrayList(token);
    }

    /**
     * <p>
     * 所有请求-头部-灰度必填
     * </p>
     *
     * @return /
     */
    private List<SecurityReference> grayHeader() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        //gray
        SecurityReference token = new SecurityReference(SecurityConstant.FEIGN_GRAY_TAG, authorizationScopes);
        return CollectionUtil.newArrayList(token);
    }

    /**
     * <p>
     * 获取要忽略的API前缀
     * </p>
     *
     * @return 正则
     */
    private String ignoreFilterPath() {
        Set<String> ignoreFilterPath = CollUtil.newHashSet("/error.*");
        return StrUtil.join("|", ignoreFilterPath);
    }

    /**
     * <p>
     * 所有请求-头部-灰度
     * 不会把 灰度发布标识-Authorize菜单的version的值带过来
     * 但使用grayHeader()方式可解决值带过来，但它又是必填，默认值又不能设置
     * </p>
     *
     * @return header
     */
    @Deprecated
    private List<RequestParameter> grayHeaders() {
        List<RequestParameter> pars = new ArrayList<>();
        RequestParameter parameter = new RequestParameterBuilder().name(SecurityConstant.FEIGN_GRAY_TAG).description("灰度标识").required(false).in(ParameterType.HEADER).query(q -> q.model(m -> m.scalarModel(ScalarType.STRING))).required(false).build();
        pars.add(parameter);
        return pars;
    }

    /**
     * 增加如下配置可解决Spring Boot 6.x 与Swagger 3.0.0 不兼容问题
     **/
    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties, Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
    }

    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
        return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
    }
}

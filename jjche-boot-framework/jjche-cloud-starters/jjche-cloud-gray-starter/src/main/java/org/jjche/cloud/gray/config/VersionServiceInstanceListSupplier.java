package org.jjche.cloud.gray.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.log.StaticLog;
import org.jjche.common.constant.SecurityConstant;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestDataContext;
import org.springframework.cloud.loadbalancer.core.DelegatingServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 自定义服务实例筛选逻辑
 * </p>
 *
 * @author miaoyj
 * @since 2022-07-26
 */
public class VersionServiceInstanceListSupplier extends DelegatingServiceInstanceListSupplier {


    public VersionServiceInstanceListSupplier(ServiceInstanceListSupplier delegate) {
        super(delegate);
    }


    @Override
    public Flux<List<ServiceInstance>> get() {
        return delegate.get();
    }

    @Override
    public Flux<List<ServiceInstance>> get(Request request) {
        return delegate.get(request).map(instances -> filteredByVersion(instances, getVersion(request.getContext())));
    }


    /**
     * filter instance by requestVersion
     */
    private List<ServiceInstance> filteredByVersion(List<ServiceInstance> instances, String requestVersion) {
        StaticLog.debug("request version is {}", requestVersion);
        if (StrUtil.isEmpty(requestVersion)) {
            return instances;
        }

        List<ServiceInstance> filteredInstances = instances.stream()
                .filter(instance -> requestVersion.equalsIgnoreCase(instance.getMetadata().getOrDefault(SecurityConstant.FEIGN_GRAY_TAG, "")))
                .collect(Collectors.toList());

        if (filteredInstances.size() > 0) {
            return filteredInstances;
        }

        return instances;
    }

    private String getVersion(Object requestContext) {
        if (requestContext == null) {
            return null;
        }
        String version = null;
        if (requestContext instanceof RequestDataContext) {
            version = getVersionFromHeader((RequestDataContext) requestContext);
        }
        return version;
    }

    /**
     * get version from header
     */
    private String getVersionFromHeader(RequestDataContext context) {
        if (context.getClientRequest() != null) {
            HttpHeaders headers = context.getClientRequest().getHeaders();
            if (headers != null) {
                //could extract to the properties
                String metadataVersion = headers.getFirst(SecurityConstant.FEIGN_GRAY_TAG);
                //这个判断解决，由于没有指定version，灰度实例会被访问到，这里默认指定一个
                if (StrUtil.isBlank(metadataVersion)) {
                    metadataVersion = SpringUtil.getProperty("spring.cloud.nacos.discovery.metadata.version");
                }
                return metadataVersion;
            }
        }
        return null;
    }
}

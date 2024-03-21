package org.jjche.sys.modules.mnt.api.dto;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <p>DeployDTO class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Data
public class DeployDTO implements Serializable {

    /**
     * 部署编号
     */
    private String id;

    private AppDTO app;

    /**
     * 服务器
     */
    @SuppressWarnings("MapOrSetKeyShouldOverrideHashCodeEquals")
    private Set<ServerDeployDTO> deploys;

    private String servers;

    /**
     * 服务状态
     */
    private String status;

    private Timestamp gmtCreate;

    /**
     * <p>Getter for the field <code>servers</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getServers() {
        if (CollectionUtil.isNotEmpty(deploys)) {
            return deploys.stream().map(ServerDeployDTO::getName).collect(Collectors.joining(","));
        }
        return servers;
    }
}

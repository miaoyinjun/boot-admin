package org.jjche.sys.modules.mnt.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>DeployHistoryDTO class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Data
public class DeployHistoryDTO implements Serializable {

    /**
     * 编号
     */
    private String id;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 部署IP
     */
    private String ip;

    /**
     * 部署时间
     */
    private Timestamp gmtCreate;

    /**
     * 部署人员
     */
    private String deployUser;

    /**
     * 部署编号
     */
    private Long deployId;
}

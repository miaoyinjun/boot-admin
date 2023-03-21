package org.jjche.system.modules.mnt.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>ServerDeployDTO class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Data
public class ServerDeployDTO implements Serializable {

    private Long id;

    private String name;

    private String ip;

    private Integer port;

    private String account;

    private String password;

    private Timestamp gmtCreate;
}

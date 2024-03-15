package org.jjche.system.modules.logging.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>LogErrorDTO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-5-22
 */
@Data
public class LogErrorDTO implements Serializable {

    private Long id;

    private String username;

    private String description;

    private String method;

    private String params;

    private String browser;

    private String requestIp;

    private String address;

    private Timestamp gmtCreate;

    private String userAgent;

    private String os;
}

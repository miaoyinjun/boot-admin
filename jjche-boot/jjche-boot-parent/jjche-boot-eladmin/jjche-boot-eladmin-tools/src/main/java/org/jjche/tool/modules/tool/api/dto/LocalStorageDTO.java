package org.jjche.tool.modules.tool.api.dto;

import lombok.Data;
import org.jjche.common.enums.FileType;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>LocalStorageDTO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-09-05
 */
@Data
public class LocalStorageDTO implements Serializable {

    private String id;

    private String realName;

    private String name;

    private String suffix;

    private FileType type;

    private String size;

    private Timestamp gmtCreate;

    private String createdBy;
}

package org.jjche.system.modules.system.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>JobDTO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-03-29
 */
@Data
@NoArgsConstructor
public class JobDTO implements Serializable {

    private Long id;

    private Integer jobSort;

    private String name;

    private Boolean enabled;

    private Timestamp gmtCreate;
}

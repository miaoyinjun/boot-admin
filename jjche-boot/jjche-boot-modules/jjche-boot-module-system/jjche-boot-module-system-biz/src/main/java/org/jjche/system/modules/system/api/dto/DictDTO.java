package org.jjche.system.modules.system.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>DictDTO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-04-10
 */
@Data
public class DictDTO implements Serializable {

    private Long id;

    private String name;

    private String description;
}

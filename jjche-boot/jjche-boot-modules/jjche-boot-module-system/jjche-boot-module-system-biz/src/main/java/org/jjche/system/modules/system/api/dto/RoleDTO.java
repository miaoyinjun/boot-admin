package org.jjche.system.modules.system.api.dto;

import lombok.Data;
import org.jjche.system.modules.system.api.enums.DataScopeEnum;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
 * <p>RoleDTO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-23
 */
@Data
public class RoleDTO implements Serializable {

    private Long id;

    private Set<MenuDTO> menus;

    private Set<DeptDTO> depts;

    private String name;

    private DataScopeEnum dataScope;
    private String dataScopeValue;

    private Integer level;

    private String description;

    private String code;

    private Timestamp gmtCreate;
}

package org.jjche.sys.modules.system.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * <p>MenuDTO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-12-17
 */
@Data
public class MenuDTO implements Serializable {

    private Long id;

    private List<MenuDTO> children;

    private Integer type;

    private String permission;

    private String title;

    private Integer menuSort;

    private String path;

    private String component;

    private Long pid;

    private Integer subCount;

    private Boolean iFrame;

    @ApiModelProperty(value = "数据权限")
    private Boolean isDataPermission;

    private Boolean cache;

    private Boolean hidden;

    private String componentName;

    private String icon;

    private Timestamp gmtCreate;

    /**
     * <p>getHasChildren.</p>
     *
     * @return a {@link java.lang.Boolean} object.
     */
    public Boolean getHasChildren() {
        return subCount > 0;
    }

    /**
     * <p>getLeaf.</p>
     *
     * @return a {@link java.lang.Boolean} object.
     */
    public Boolean getLeaf() {
        return subCount <= 0;
    }

    /**
     * <p>getLabel.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLabel() {
        return title;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuDTO menuDto = (MenuDTO) o;
        return Objects.equals(id, menuDto.id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

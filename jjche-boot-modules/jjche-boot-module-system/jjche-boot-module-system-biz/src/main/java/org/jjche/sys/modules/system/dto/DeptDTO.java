package org.jjche.sys.modules.system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.jjche.common.annotation.JacksonAllowNull;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * <p>DeptDTO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-03-25
 */
@Data
public class DeptDTO implements Serializable {

    private Long id;

    private String name;

    private Boolean enabled;

    private Integer deptSort;

    private Timestamp gmtCreate;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JacksonAllowNull
    private List<DeptDTO> children;

    @JacksonAllowNull
    private Long pid;

    private Integer subCount;

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
        return name;
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
        DeptDTO deptDto = (DeptDTO) o;
        return Objects.equals(id, deptDto.id) &&
                Objects.equals(name, deptDto.name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

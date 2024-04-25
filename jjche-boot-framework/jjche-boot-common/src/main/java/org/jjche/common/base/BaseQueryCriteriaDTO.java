package org.jjche.common.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.jjche.common.annotation.IgnoreField;
import org.jjche.common.dto.PermissionDataRuleDTO;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 查询基类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-10-22
 */
@Data
public class BaseQueryCriteriaDTO implements Serializable {

    @IgnoreField
    @JsonIgnore
    private List<PermissionDataRuleDTO> permissionDataRuleList;
}

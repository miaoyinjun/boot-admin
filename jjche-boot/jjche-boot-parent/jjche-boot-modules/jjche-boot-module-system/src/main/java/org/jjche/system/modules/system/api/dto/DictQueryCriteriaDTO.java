package org.jjche.system.modules.system.api.dto;

import lombok.Data;
import org.jjche.common.dto.BaseQueryCriteriaDTO;

/**
 * <p>DictQueryCriteriaDTO class.</p>
 *
 * @author Zheng Jie
 * 公共查询类
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class DictQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    private String blurry;
}

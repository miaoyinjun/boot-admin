package org.jjche.sys.modules.system.dto;

import lombok.Data;
import org.jjche.common.base.BaseQueryCriteriaDTO;

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

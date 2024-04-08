package org.jjche.sys.modules.tool.dto;

import lombok.Data;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.base.BaseQueryCriteriaDTO;

import java.util.List;

/**
 * <p>QiniuQueryCriteriaDTO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-6-4 09:54:37
 */
@Data
public class QiniuQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    @QueryCriteria(type = QueryCriteria.Type.LIKE)
    private String key;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;
}

package org.jjche.sys.modules.mnt.dto;

import lombok.Data;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.base.BaseQueryCriteriaDTO;

import java.util.List;

/**
 * <p>DatabaseQueryCriteriaDTO class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Data
public class DatabaseQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    /**
     * 模糊
     */
    @QueryCriteria(type = QueryCriteria.Type.LIKE)
    private String name;

    /**
     * 精确
     */
    @QueryCriteria
    private String jdbcUrl;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;
}

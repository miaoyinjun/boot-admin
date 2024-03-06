package org.jjche.system.modules.logging.api.dto;

import lombok.Data;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.base.BaseQueryCriteriaDTO;

import java.util.List;

/**
 * 日志查询类
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-6-4 09:23:07
 */
@Data
public class LogQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    private String blurry;

    @QueryCriteria
    private String username;

    @QueryCriteria
    private String logType;

    @QueryCriteria
    private Boolean isSuccess;

    @QueryCriteria
    private String module;

    @QueryCriteria
    private String category;

    @QueryCriteria
    private String appName;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;
}

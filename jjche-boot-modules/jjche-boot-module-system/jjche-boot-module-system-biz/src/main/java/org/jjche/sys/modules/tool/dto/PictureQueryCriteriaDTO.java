package org.jjche.sys.modules.tool.dto;

import lombok.Data;
import org.jjche.common.base.BaseQueryCriteriaDTO;

import java.util.List;

/**
 * sm.ms图床
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-6-4 09:52:09
 */
@Data
public class PictureQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    private String filename;

    private String username;

    private List<String> gmtCreate;
}

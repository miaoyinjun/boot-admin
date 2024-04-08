package org.jjche.sys.modules.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>MenuMetaVO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-12-20
 */
@Data
@AllArgsConstructor
public class MenuMetaVO implements Serializable {

    private String title;

    private String icon;

    private Boolean noCache;
}

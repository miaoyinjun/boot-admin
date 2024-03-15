package org.jjche.system.modules.generator.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表的数据信息
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableInfo {

    /**
     * 表名称
     */
    private Object tableName;

    /**
     * 创建日期
     */
    private Object gmtCreate;

    /**
     * 数据库引擎
     */
    private Object engine;

    /**
     * 编码集
     */
    private Object coding;

    /**
     * 备注
     */
    private Object remark;


}

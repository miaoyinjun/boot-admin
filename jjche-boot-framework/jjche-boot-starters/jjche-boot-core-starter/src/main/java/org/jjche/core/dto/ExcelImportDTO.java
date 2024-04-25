package org.jjche.core.dto;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 数据导入
 * </p>
 *
 * @author sun_haitao
 * @since 2021-04-12
 */
@Data
public class ExcelImportDTO implements IExcelModel, IExcelDataModel, Comparable<ExcelImportDTO>, Serializable {
    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 错误数据的行号
     */
    private Integer rowNum;

    @Override
    public int compareTo(ExcelImportDTO o) {
        return this.rowNum.compareTo(o.rowNum);
    }
}
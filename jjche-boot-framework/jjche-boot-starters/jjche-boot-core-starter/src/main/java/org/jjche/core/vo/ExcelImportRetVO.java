package org.jjche.core.vo;

import lombok.Data;
import org.jjche.core.dto.ExcelImportDTO;

/**
 * <p>
 * 数据导入返回
 * </p>
 *
 * @author sun_haitao
 * @since 2021-04-12
 */
@Data
public class ExcelImportRetVO {
    protected String errMsg;
    protected Integer rowNum;

    public ExcelImportRetVO() {
    }

    public ExcelImportRetVO(ExcelImportDTO importReq) {
        this.errMsg = importReq.getErrorMsg();
        this.rowNum = importReq.getRowNum() + 1;
    }

    public ExcelImportRetVO(ExcelImportDTO error, ExcelImportDTO item) {
        this.rowNum = item.getRowNum() + 1;
        Integer errRowNum = error.getRowNum() + 1;
        this.errMsg = "与第" + errRowNum + "行数据重复";
    }

    public ExcelImportRetVO(String errMsg, ExcelImportDTO importReq) {
        this.rowNum = importReq.getRowNum() + 1;
        this.errMsg = errMsg;
    }

    public ExcelImportRetVO(String errMsg) {
        this.errMsg = errMsg;
    }
}

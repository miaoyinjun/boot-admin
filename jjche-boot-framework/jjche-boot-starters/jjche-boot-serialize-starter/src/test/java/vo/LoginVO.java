package vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jjche.common.annotation.JacksonAllowNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 登录出参
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Data
@NoArgsConstructor
public class LoginVO implements Serializable {
    private String name;
    @JacksonAllowNull
    private String name2;
    private String telephone;

    private Boolean aBoolean;
    private Long aLong;
    private Integer aInteger;
    private Float aFloat;
    private Double aDouble;
    private BigDecimal aBigDecimal;
    private List<String> aList;

}

package desgin.strategy.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO implements Serializable {
    private String code;
    private BigDecimal price;

    /**
     * 订单类型
     * 1：普通订单;
     * 2：团购订单;
     * 3：促销订单;
     */
    private String type;
}

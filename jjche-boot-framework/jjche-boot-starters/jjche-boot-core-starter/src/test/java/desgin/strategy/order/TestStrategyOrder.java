package desgin.strategy.order;

import cn.hutool.core.util.NumberUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * http://www.ciphermagic.cn/spring-boot-without-if-else.html
 */
@SpringBootTest(classes = {OrderServiceImpl.class, HandlerProcessor.class, BeanTool.class,
        NormalHandler.class, GroupHandler.class, PromotionHandler.class})
public class TestStrategyOrder {

    @Autowired
    private IOrderService orderService;

    @Test
    public void Test1() {
        OrderDTO orderDTO = new OrderDTO(AbstractHandler.HANDLER_TYPE_NORMA, NumberUtil.add(AbstractHandler.HANDLER_TYPE_NORMA), AbstractHandler.HANDLER_TYPE_NORMA);
        OrderDTO orderDTO2 = new OrderDTO(AbstractHandler.HANDLER_TYPE_GROUP, NumberUtil.add(AbstractHandler.HANDLER_TYPE_GROUP), AbstractHandler.HANDLER_TYPE_GROUP);
        OrderDTO orderDTO3 = new OrderDTO(AbstractHandler.HANDLER_TYPE_PROMOTION, NumberUtil.add(AbstractHandler.HANDLER_TYPE_PROMOTION), AbstractHandler.HANDLER_TYPE_PROMOTION);

        String result = orderService.handle(orderDTO);
        assertEquals(result, AbstractHandler.HANDLER_MSG_NORMAL);

        result = orderService.handle(orderDTO2);
        assertEquals(result, AbstractHandler.HANDLER_MSG_GROUP);

        result = orderService.handle(orderDTO3);
        assertEquals(result, AbstractHandler.HANDLER_MSG_PROMOTION);
    }

}
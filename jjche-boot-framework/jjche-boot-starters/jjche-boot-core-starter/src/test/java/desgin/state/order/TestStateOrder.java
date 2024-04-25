package desgin.state.order;

import desgin.strategy.order.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * https://www.cnblogs.com/yulibostu/articles/10774936.html
 */
@SpringBootTest(classes = {OrderServiceImpl.class, HandlerProcessor.class, BeanTool.class,
        NormalHandler.class, GroupHandler.class, PromotionHandler.class})
public class TestStateOrder {

    @Autowired
    private IOrderService orderService;

    @Test
    public void Test1() {
        OrderContext orderContext = new OrderContext();
        orderContext.confirm();     //已预定状态>已确认状态
        orderContext.modify();      //已确认状态>已预定状态
        orderContext.confirm();    //已预定状态>已确认状态
        orderContext.pay();       //已确认状态>已锁定状态
        orderContext.modify();
    }

}
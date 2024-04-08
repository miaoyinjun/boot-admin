package desgin.strategy.order;

public abstract class AbstractHandler {
    static final String HANDLER_MSG_NORMAL = "处理普通订单";
    static final String HANDLER_MSG_GROUP = "处理团购订单";
    static final String HANDLER_MSG_PROMOTION = "处理促销订单";

    static final String HANDLER_TYPE_NORMA = "1";
    static final String HANDLER_TYPE_GROUP = "2";
    static final String HANDLER_TYPE_PROMOTION = "3";

    abstract public String handle(OrderDTO orderDTO);
}

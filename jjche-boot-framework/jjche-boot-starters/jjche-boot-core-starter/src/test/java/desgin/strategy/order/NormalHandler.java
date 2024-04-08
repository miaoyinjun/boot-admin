package desgin.strategy.order;

import org.springframework.stereotype.Component;

@Component
@HandlerType(AbstractHandler.HANDLER_TYPE_NORMA)
public class NormalHandler extends AbstractHandler {

    @Override
    public String handle(OrderDTO orderDTO) {
        return this.HANDLER_MSG_NORMAL;
    }
}

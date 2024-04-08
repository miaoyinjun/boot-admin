package desgin.state.order;

abstract class OrderState {
    abstract void confirm(OrderContext orderContext);

    abstract void modify(OrderContext orderContext);

    abstract void pay(OrderContext orderContext);
}

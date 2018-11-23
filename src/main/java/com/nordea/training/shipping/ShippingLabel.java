package com.nordea.training.shipping;

import com.nordea.training.avromodel.Order;
import com.nordea.training.avromodel.User;

public class ShippingLabel {
    private User user;
    private Order order;

    public ShippingLabel(User user, Order order) {
        this.user = user;
        this.order = order;
    }

    @Override
    public String toString() {
        return "ShippingLabel{" +
                "user=" + user +
                ", order=" + order +
                '}';
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

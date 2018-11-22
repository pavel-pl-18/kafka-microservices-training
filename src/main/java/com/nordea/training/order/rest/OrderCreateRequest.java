package com.nordea.training.order.rest;

public class OrderCreateRequest {
    private Long productId;
    private Integer quantity;
    private Long userId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "OrderCreateRequest{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                ", userId=" + userId +
                '}';
    }
}

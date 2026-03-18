package com.example.demo.dto;

import java.util.List;

/**
 * 下单请求 DTO（和 mysql-learning 一模一样）
 */
public class CreateOrderRequest {

    private Long userId;
    private List<OrderItemDTO> items;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }

    public static class OrderItemDTO {
        private Long productId;
        private Integer quantity;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
}

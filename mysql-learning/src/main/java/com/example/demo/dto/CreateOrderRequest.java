package com.example.demo.dto;

import java.util.List;

/**
 * 下单请求 DTO（Data Transfer Object）
 *
 * 作用：接收前端传来的下单参数
 * 类比前端：interface CreateOrderRequest { ... }
 */
public class CreateOrderRequest {

    private Long userId;
    private List<OrderItemDTO> items;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    /**
     * 订单商品项
     */
    public static class OrderItemDTO {
        private Long productId;
        private Integer quantity;

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
    }
}

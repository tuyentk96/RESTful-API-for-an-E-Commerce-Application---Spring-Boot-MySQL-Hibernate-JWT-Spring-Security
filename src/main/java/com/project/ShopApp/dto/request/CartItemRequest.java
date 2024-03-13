package com.project.ShopApp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.ShopApp.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequest {
    @JsonProperty("product_id")
    private Long productId;
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("color")
    private String color;
}

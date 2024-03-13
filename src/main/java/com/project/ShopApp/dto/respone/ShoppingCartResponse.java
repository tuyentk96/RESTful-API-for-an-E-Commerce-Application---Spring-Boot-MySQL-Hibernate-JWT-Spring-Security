package com.project.ShopApp.dto.respone;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.ShopApp.models.CartItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Builder
public class ShoppingCartResponse {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("list_cart_item")
    private List<CartItem> itemList;
}

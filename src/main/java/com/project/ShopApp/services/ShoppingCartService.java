package com.project.ShopApp.services;

import com.project.ShopApp.dto.request.CartItemRequest;
import com.project.ShopApp.dto.respone.ShoppingCartResponse;
import com.project.ShopApp.models.CartItem;
import com.project.ShopApp.models.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    ShoppingCartResponse addCart(Long userId, CartItemRequest cartItemRequest);

    void deleteCart(Long shoppingCartId);

    List<CartItem> getListCartItem(Long userId);
}

package com.project.ShopApp.controllers;

import com.project.ShopApp.dto.request.CartItemRequest;
import com.project.ShopApp.dto.respone.ShoppingCartResponse;
import com.project.ShopApp.exception.SuccessResponse;
import com.project.ShopApp.exception.SuccessResult;
import com.project.ShopApp.models.CartItem;
import com.project.ShopApp.services.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{user-id}")
    public ResponseEntity<ShoppingCartResponse>  addCart(@PathVariable("user-id") Long userId,
                                                         @ModelAttribute CartItemRequest cartItemRequest){
        return ResponseEntity.ok(shoppingCartService.addCart(userId,cartItemRequest));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{shopping-cart-id}")
    public ResponseEntity<SuccessResponse> deleteCart(@PathVariable("shopping-cart-id") Long shoppingCartId){
        shoppingCartService.deleteCart(shoppingCartId);
        return ResponseEntity.ok(new SuccessResponse(SuccessResult.DELETE_SUCCESS));
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<List<CartItem>> getListCartItem(@PathVariable("user-id") Long userId){
        return ResponseEntity.ok(shoppingCartService.getListCartItem(userId));
    }
}

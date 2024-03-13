package com.project.ShopApp.repositories;

import com.project.ShopApp.models.CartItem;
import com.project.ShopApp.models.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    List<CartItem> findAllByShoppingCartId(Long id);

    CartItem findByShoppingCartIdAndProductId(Long id, Long productId);

    void deleteAllByShoppingCartId(Long shoppingCartId);
}

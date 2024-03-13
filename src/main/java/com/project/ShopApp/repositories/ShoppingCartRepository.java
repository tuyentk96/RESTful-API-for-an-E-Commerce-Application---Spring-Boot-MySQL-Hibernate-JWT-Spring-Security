package com.project.ShopApp.repositories;

import com.project.ShopApp.models.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    ShoppingCart findByUserId(Long userId);
}

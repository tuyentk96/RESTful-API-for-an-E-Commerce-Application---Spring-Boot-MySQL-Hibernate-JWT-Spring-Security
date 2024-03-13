package com.project.ShopApp.repositories;

import com.project.ShopApp.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findAllByUserId(Long userId);
    Page<Order> findAll(Pageable pageable);
}

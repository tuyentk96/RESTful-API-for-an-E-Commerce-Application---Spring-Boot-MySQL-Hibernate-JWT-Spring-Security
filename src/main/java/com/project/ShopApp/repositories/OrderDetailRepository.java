package com.project.ShopApp.repositories;

import com.project.ShopApp.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    List<OrderDetail> findByOrderId(Long orderId);

    List<OrderDetail> findAllByOrderId(Long orderId);
}

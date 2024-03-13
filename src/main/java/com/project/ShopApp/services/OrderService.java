package com.project.ShopApp.services;

import com.project.ShopApp.dto.request.OrderRequest;
import com.project.ShopApp.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderRequest orderRequest);

    List<Order> getOrderByUser(Long userId);

    Order getOrder(Long orderId);

    Object updateOrder(Long orderId, OrderRequest orderRequest);

    void deleteOrder(Long orderId);

    Page<Order> getAllOrders(PageRequest pageRequest);
}

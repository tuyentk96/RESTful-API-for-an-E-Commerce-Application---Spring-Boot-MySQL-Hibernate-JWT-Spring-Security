package com.project.ShopApp.services;

import com.project.ShopApp.dto.request.OrderDetailRequest;
import com.project.ShopApp.dto.request.OrderRequest;
import com.project.ShopApp.models.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    OrderDetail createOrderDetail(OrderDetailRequest orderDetailRequest);

    List<OrderDetail> getOrderDetailsByOrderId(Long orderId);

    OrderDetail updateOrderDetail(Long orderDetailId, OrderDetailRequest orderDetailRequestRequest);

    void deleteOrderDetail(Long orderDetailId);

    OrderDetail getOrderDetail(Long orderDetailId);
}

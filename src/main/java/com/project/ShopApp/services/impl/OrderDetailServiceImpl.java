package com.project.ShopApp.services.impl;

import com.project.ShopApp.dto.request.OrderDetailRequest;
import com.project.ShopApp.dto.request.OrderRequest;
import com.project.ShopApp.exception.ErrorResponse;
import com.project.ShopApp.exception.ErrorResult;
import com.project.ShopApp.models.OrderDetail;
import com.project.ShopApp.models.Product;
import com.project.ShopApp.repositories.OrderDetailRepository;
import com.project.ShopApp.repositories.OrderRepository;
import com.project.ShopApp.repositories.ProductRepository;
import com.project.ShopApp.services.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    @Override
    public OrderDetail createOrderDetail(OrderDetailRequest orderDetailRequest) {
        OrderDetail orderDetail = new OrderDetail();

        orderDetail.setOrder(orderRepository.findById(orderDetailRequest.getOrderId())
                .orElseThrow(()->new ErrorResponse(ErrorResult.NOT_FOUND_DATA)));

        orderDetail.setColor(orderDetailRequest.getColor());
        orderDetail.setPrice(orderDetailRequest.getPrice());
        orderDetail.setTotalMoney(orderDetailRequest.getTotalMoney());
        orderDetail.setNumberOfProducts(orderDetailRequest.getNumberOfProducts());
        orderDetail.setProduct(productRepository.findById(orderDetailRequest.getOrderId())
                .orElseThrow(()->new ErrorResponse(ErrorResult.NOT_FOUND_DATA)));
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public List<OrderDetail> getOrderDetailsByOrderId(Long orderId) {
        return orderDetailRepository.findAllByOrderId(orderId);
    }

    @Override
    public OrderDetail updateOrderDetail(Long orderDetailId, OrderDetailRequest orderDetailRequest) {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(()-> new ErrorResponse(ErrorResult.NOT_FOUND_DATA));

        existingOrderDetail.setOrder(orderRepository.findById(orderDetailRequest.getOrderId())
                .orElseThrow(()->new ErrorResponse(ErrorResult.NOT_FOUND_DATA)));

        existingOrderDetail.setColor(orderDetailRequest.getColor());
        existingOrderDetail.setPrice(orderDetailRequest.getPrice());
        existingOrderDetail.setTotalMoney(orderDetailRequest.getTotalMoney());
        existingOrderDetail.setNumberOfProducts(orderDetailRequest.getNumberOfProducts());
        Product productRequest = productRepository.findById(orderDetailRequest.getProductId())
                .orElseThrow(()->new ErrorResponse(ErrorResult.NOT_FOUND_DATA));
        existingOrderDetail.setProduct(productRequest);

        return orderDetailRepository.saveAndFlush(existingOrderDetail);
    }

    @Override
    public void deleteOrderDetail(Long orderDetailId) {
        orderDetailRepository.deleteById(orderDetailId);
    }

    @Override
    public OrderDetail getOrderDetail(Long orderDetailId) {
        return orderDetailRepository.findById(orderDetailId)
                .orElseThrow(()->new ErrorResponse(ErrorResult.NOT_FOUND_DATA));
    }


}

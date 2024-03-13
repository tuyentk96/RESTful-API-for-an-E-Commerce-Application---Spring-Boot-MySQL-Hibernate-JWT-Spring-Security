package com.project.ShopApp.services.impl;

import com.project.ShopApp.dto.request.OrderRequest;
import com.project.ShopApp.exception.ErrorResponse;
import com.project.ShopApp.exception.ErrorResult;
import com.project.ShopApp.models.*;
import com.project.ShopApp.repositories.*;
import com.project.ShopApp.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderDetailRepository orderDetailRepository;
    @Override
    public Order createOrder(OrderRequest orderRequest) {
        User user = userRepository
                .findById(orderRequest.getUserId())
                .orElseThrow(() -> new ErrorResponse(ErrorResult.NOT_FOUND_USER));
        //convert orderRequest => Order
        //dùng thư viện Model Mapper
        // Tạo một luồng bảng ánh xạ riêng để kiểm soát việc ánh xạ
        modelMapper.typeMap(OrderRequest.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        // Cập nhật các trường của đơn hàng từ orderRequest
        Order order = new Order();
        modelMapper.map(orderRequest, order);
        order.setUser(user);
        order.setOrderDate(new Date());//lấy thời điểm hiện tại
        order.setStatus(String.valueOf(OrderStatus.pending));
        //Kiểm tra shipping date phải >= ngày hôm nay
        LocalDate shippingDate = orderRequest.getShippingDate() == null
                ? LocalDate.now() : orderRequest.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new ErrorResponse(ErrorResult.DATE_ERROR);
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(orderRequest.getUserId());

        List<CartItem> cartItems = cartItemRepository.findAllByShoppingCartId(shoppingCart.getId());
        List<OrderDetail> orderDetails = new ArrayList<>();

        Float totalMoney = 0F ;

        for (CartItem cartItem : cartItems){
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .color(cartItem.getColor())
                    .price(cartItem.getProduct().getPrice())
                    .product(cartItem.getProduct())
                    .numberOfProducts(cartItem.getQuantity())
                    .totalMoney(cartItem.getProduct().getPrice()*cartItem.getQuantity())
                    .build();
            orderDetails.add(orderDetail);
            totalMoney += cartItem.getProduct().getPrice()*cartItem.getQuantity();
        }

        order.setOrderDetails(orderDetails);
        order.setTotalMoney(totalMoney);
        orderRepository.saveAndFlush(order);
        orderDetailRepository.saveAll(orderDetails);

        cartItemRepository.deleteAll(cartItems);
        shoppingCartRepository.delete(shoppingCart);


        return order;
    }

    @Override
    public List<Order> getOrderByUser(Long userId) {
        userRepository.findById(userId).orElseThrow(()-> new ErrorResponse(ErrorResult.NOT_FOUND_USER));
        return orderRepository.findAllByUserId(userId);
    }

    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()-> new ErrorResponse(ErrorResult.NOT_FOUND_DATA));
    }

    @Override
    public Object updateOrder(Long orderId, OrderRequest orderRequest) {
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new ErrorResponse(ErrorResult.NOT_FOUND_DATA));
        User existingUser = userRepository.findById(orderRequest.getUserId()).orElseThrow(()-> new ErrorResponse(ErrorResult.NOT_FOUND_USER));
        modelMapper.typeMap(OrderRequest.class,Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderRequest,order);
        order.setUser(existingUser);

        return orderRepository.saveAndFlush(order);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new ErrorResponse(ErrorResult.NOT_FOUND_DATA));
        order.setActive(false);
        orderRepository.saveAndFlush(order);
    }

    @Override
    public Page<Order> getAllOrders(PageRequest pageRequest) {
        return orderRepository.findAll(pageRequest);
    }

}

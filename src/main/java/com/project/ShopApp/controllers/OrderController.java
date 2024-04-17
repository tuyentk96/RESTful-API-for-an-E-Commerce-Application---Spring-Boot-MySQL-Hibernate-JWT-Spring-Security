package com.project.ShopApp.controllers;

import com.project.ShopApp.dto.request.OrderRequest;
import com.project.ShopApp.dto.respone.ListOrderResponse;
import com.project.ShopApp.dto.respone.ValidatedErrorResponse;
import com.project.ShopApp.exception.SuccessResponse;
import com.project.ShopApp.exception.SuccessResult;
import com.project.ShopApp.models.Order;
import com.project.ShopApp.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderRequest orderRequest, BindingResult result){
        if (result.hasErrors()){
           return ValidatedErrorResponse.validatedErrorResponse(result);
        }
        return ResponseEntity.ok(new SuccessResponse(SuccessResult.CREATED_SUCCESS,orderService.createOrder(orderRequest)));
    }

    @GetMapping("/user/{user-id}")
    public ResponseEntity<List<Order>> getOrderByUser(@PathVariable("user-id") Long userId){
        return ResponseEntity.ok(orderService.getOrderByUser(userId));
    }

    @GetMapping("/{order-id}")
    public ResponseEntity<Order> getOrder(@PathVariable("order-id") Long orderId){
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @GetMapping("")
    public ResponseEntity<ListOrderResponse> getAllOrder(@RequestParam("page") int page,
                                                         @RequestParam("limit") int limit){
        PageRequest pageRequest = PageRequest.of(page, limit,
                Sort.by("orderDate").descending());
        Page<Order> orderPage = orderService.getAllOrders(pageRequest);
        List<Order> orders = orderPage.getContent();
        int totalPage = orderPage.getTotalPages();
        return ResponseEntity.ok(new ListOrderResponse(totalPage,orders));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{order-id}")
    public ResponseEntity<?> updateOrder(@PathVariable("order-id") Long orderId,
                                             @RequestBody @Valid OrderRequest orderRequest,
                                             BindingResult result){
        if (result.hasErrors()){
            return ValidatedErrorResponse.validatedErrorResponse(result);
        }
        return ResponseEntity.ok(new SuccessResponse(SuccessResult.UPDATE_SUCCESS,orderService.updateOrder(orderId,orderRequest)));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{order-id}")
    public ResponseEntity<SuccessResponse> deleteOrder(@PathVariable("order-id") Long orderId){
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok(new SuccessResponse(SuccessResult.DELETE_SUCCESS));
    }
}

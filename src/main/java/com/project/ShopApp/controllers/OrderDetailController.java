package com.project.ShopApp.controllers;

import com.project.ShopApp.dto.request.OrderDetailRequest;
import com.project.ShopApp.dto.request.OrderRequest;
import com.project.ShopApp.dto.respone.ValidatedErrorResponse;
import com.project.ShopApp.exception.SuccessResponse;
import com.project.ShopApp.exception.SuccessResult;
import com.project.ShopApp.models.OrderDetail;
import com.project.ShopApp.services.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/order-details")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailRequest orderDetailRequest,
            BindingResult result){
        if (result.hasErrors()){
            return ValidatedErrorResponse.validatedErrorResponse(result);
        }
        return ResponseEntity.ok(new SuccessResponse(SuccessResult.CREATED_SUCCESS,
                orderDetailService.createOrderDetail(orderDetailRequest)));
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/order/{order-id}")
    public ResponseEntity<List<OrderDetail>> getOrderDetailsByOrderId(@PathVariable("order-id") Long orderId){
        return ResponseEntity.ok(orderDetailService.getOrderDetailsByOrderId(orderId));
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{order-detail-id}")
    public ResponseEntity<OrderDetail> getOrderDetail(@PathVariable("order-detail-id") Long orderDetailId){

        return ResponseEntity.ok(orderDetailService.getOrderDetail(orderDetailId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{order-detail-id}")
    public ResponseEntity<?> updateOrderDetail(
            @PathVariable("order-detail-id") Long orderDetailId,
            @RequestBody @Valid OrderDetailRequest orderDetailRequestRequest,
            BindingResult result
    ){
        if (result.hasErrors()){
            return ValidatedErrorResponse.validatedErrorResponse(result);
        }
        return ResponseEntity.ok(new SuccessResponse(SuccessResult.UPDATE_SUCCESS,
                orderDetailService.updateOrderDetail(orderDetailId,orderDetailRequestRequest)));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{order-detail-id}")
    public ResponseEntity<SuccessResponse> deleteOrderDetail(@PathVariable("order-detail-id") Long id){
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok(new SuccessResponse(SuccessResult.DELETE_SUCCESS));
    }


}

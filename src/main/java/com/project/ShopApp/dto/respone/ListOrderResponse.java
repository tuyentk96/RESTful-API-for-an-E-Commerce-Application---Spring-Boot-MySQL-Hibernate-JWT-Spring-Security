package com.project.ShopApp.dto.respone;

import com.project.ShopApp.models.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class ListOrderResponse {
    private int totalPage;
    private List<Order> orders;
}

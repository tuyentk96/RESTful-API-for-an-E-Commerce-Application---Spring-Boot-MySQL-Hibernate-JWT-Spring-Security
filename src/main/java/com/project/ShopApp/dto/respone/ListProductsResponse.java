package com.project.ShopApp.dto.respone;

import com.project.ShopApp.models.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class ListProductsResponse {
    private int totalPage;
    private List<Product> products;
}

package com.project.ShopApp.services;

import com.project.ShopApp.models.ProductImage;

import java.util.List;

public interface ProductImageService {
    List<ProductImage> getByProductId(Long productId);
}

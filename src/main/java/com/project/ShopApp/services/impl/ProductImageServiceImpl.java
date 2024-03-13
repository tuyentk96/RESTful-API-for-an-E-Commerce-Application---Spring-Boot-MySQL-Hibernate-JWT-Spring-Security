package com.project.ShopApp.services.impl;

import com.project.ShopApp.models.ProductImage;
import com.project.ShopApp.repositories.ProductImageRepository;
import com.project.ShopApp.services.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageRepository productImageRepository;
    @Override
    public List<ProductImage> getByProductId(Long productId) {
        return productImageRepository.findByProductId(productId);
    }
}

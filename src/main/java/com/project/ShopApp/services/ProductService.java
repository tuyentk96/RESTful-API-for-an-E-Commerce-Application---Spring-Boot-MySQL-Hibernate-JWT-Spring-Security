package com.project.ShopApp.services;

import com.project.ShopApp.dto.request.ProductRequest;
import com.project.ShopApp.dto.respone.CreateProductResponse;
import com.project.ShopApp.dto.respone.UpdateProductResponse;
import com.project.ShopApp.models.Product;
import com.project.ShopApp.models.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    CreateProductResponse createProduct(ProductRequest productRequest);
    Product getProductById(Long id);
    Page<Product> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest);
    UpdateProductResponse updateProduct(Long id, ProductRequest productRequest);
    void deleteProduct(Long id);
    boolean existsByName(String name);

    List<ProductImage> createProductImage(Long productId, List<MultipartFile> files) throws IOException;

    Product updateThumbnailProduct(Long productImageId);
}

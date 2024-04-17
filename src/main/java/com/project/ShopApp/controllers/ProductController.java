package com.project.ShopApp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import com.project.ShopApp.dto.request.ProductRequest;
import com.project.ShopApp.dto.respone.CreateProductResponse;
import com.project.ShopApp.dto.respone.ListProductsResponse;
import com.project.ShopApp.dto.respone.UpdateProductResponse;
import com.project.ShopApp.dto.respone.ValidatedErrorResponse;
import com.project.ShopApp.exception.ErrorResponse;
import com.project.ShopApp.exception.ErrorResult;
import com.project.ShopApp.exception.SuccessResponse;
import com.project.ShopApp.exception.SuccessResult;
import com.project.ShopApp.models.Product;
import com.project.ShopApp.models.ProductImage;
import com.project.ShopApp.services.ProductImageService;
import com.project.ShopApp.services.ProductRedisService;
import com.project.ShopApp.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final ProductImageService productImageService;
    private final ProductRedisService productRedisService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductRequest productRequest,
            BindingResult result){
        if (result.hasErrors()){
            return ValidatedErrorResponse.validatedErrorResponse(result);
        }
        CreateProductResponse createProductResponse = productService.createProduct(productRequest);
        productRedisService.clear();
        return ResponseEntity.ok(createProductResponse);
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<Product> getProductById(@PathVariable("product-id") Long productId){
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @GetMapping("")
    public ResponseEntity<ListProductsResponse> getProducts(
        @RequestParam(defaultValue = "0", name = "page") int page,
        @RequestParam(defaultValue = "10", name = "limit") int limit,
        @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
        @RequestParam(defaultValue = "") String keyword
    ) throws JsonProcessingException {
        int totalPage = 0;
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("createdAt").descending());

        log.info(String.format("keyword = %s , category_id = %d , page = %d , limit = %d",
                keyword, categoryId, page, limit));

        List<Product> products = productRedisService.getAllProducts(keyword,categoryId,pageRequest);

        if (products == null || products.isEmpty()){
            Page<Product> productPage = productService.getAllProducts(keyword,categoryId,pageRequest);
            products = productPage.getContent();
            totalPage = productPage.getTotalPages();
            productRedisService.saveAllProducts(products,keyword,categoryId,pageRequest);
        }

        return ResponseEntity.ok(new ListProductsResponse(totalPage,products));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{product-id}")
    public ResponseEntity<?> updateProductById(@PathVariable("product-id") Long productId,
                                                                   @Valid @RequestBody ProductRequest productRequest,
                                                                   BindingResult result){
        if (result.hasErrors()){
            return ValidatedErrorResponse.validatedErrorResponse(result);
        }
        UpdateProductResponse updateProductResponse = productService.updateProduct(productId,productRequest);
        productRedisService.clear();
        return ResponseEntity.ok(updateProductResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{product-id}")
    public ResponseEntity<SuccessResponse> deleteProductById(@PathVariable("product-id") Long productId){
        productService.deleteProduct(productId);
        productRedisService.clear();
        return ResponseEntity.ok(new SuccessResponse(SuccessResult.DELETE_SUCCESS));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/uploads-images/{product-id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("product-id") Long productId,
            @ModelAttribute("files") List<MultipartFile> files
            ) throws IOException {
        return ResponseEntity.ok(productService.createProductImage(productId,files));
    }

    @GetMapping("/uploads-images/{product-id}")
    public ResponseEntity<List<ProductImage>> getProductImagesByProductId(@PathVariable("product-id") Long productId){
        return ResponseEntity.ok(productImageService.getByProductId(productId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/thumbnail/{product-image-id}")
    public ResponseEntity<SuccessResponse> updateThumbnailProduct(@PathVariable("product-image-id") Long productImageId){
        return ResponseEntity.ok(new SuccessResponse(SuccessResult.UPDATE_SUCCESS,productService.updateThumbnailProduct(productImageId)));
    }

    @PostMapping("/generateFakeProducts")
    public ResponseEntity<String> generateFakeProducts(){
        Faker faker = new Faker();
        for (int i = 0; i < 100000; i++) {
            String productName = faker.commerce().productName();
            if (productService.existsByName(productName)){
                continue;
            }
            ProductRequest productRequest = ProductRequest.builder()
                    .name(productName)
                    .price((float) faker.number().numberBetween(10,10000000))
                    .description(faker.lorem().sentence())
                    .categoryId((long) faker.number().numberBetween(1,5))
                    .build();
            productService.createProduct(productRequest);
        }
        return ResponseEntity.ok("Fake products created successfully");
    }
}

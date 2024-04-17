package com.project.ShopApp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.ShopApp.models.Product;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductRedisService {
    //Clear cached data in Redis
    void clear();//clear cache
    List<Product> getAllProducts(
            String keyword,
            Long categoryId, PageRequest pageRequest) throws JsonProcessingException;
    void saveAllProducts(List<Product> products,
                         String keyword,
                         Long categoryId,
                         PageRequest pageRequest) throws JsonProcessingException;

}

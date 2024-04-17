package com.project.ShopApp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ShopApp.models.Product;
import com.project.ShopApp.services.ProductRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductRedisServiceImpl implements ProductRedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;

    @Override
    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Override
    public List<Product> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest) throws JsonProcessingException {
        String key = this.getKeyFrom(keyword,categoryId,pageRequest);
        String json = (String) redisTemplate.opsForValue().get(key);
        List<Product> products = json != null
                ? redisObjectMapper.readValue(json, new TypeReference<List<Product>>(){})
                : null;
        return products;
    }

    private String getKeyFrom(String keyword, Long categoryId, PageRequest pageRequest) {
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();
        Sort sort = pageRequest.getSort();
        String sortDirection = "asc"; // Mặc định sắp xếp tăng dần
        if (sort != null && !sort.isEmpty()) {
            Sort.Order order = sort.getOrderFor("id");
            if (order != null) {
                sortDirection = order.getDirection() == Sort.Direction.DESC ? "desc" : "asc";
            }
        }
        String key = String.format("all_products:%d:%d:%s:%s:%s", pageNumber, pageSize, sortDirection, categoryId, keyword);
        return key;
    }

    @Override
    public void saveAllProducts(List<Product> products, String keyword, Long categoryId, PageRequest pageRequest) throws JsonProcessingException {
        String key = this.getKeyFrom(keyword,categoryId,pageRequest);
        String json = redisObjectMapper.writeValueAsString(products);
        redisTemplate.opsForValue().set(key, json);
    }
}

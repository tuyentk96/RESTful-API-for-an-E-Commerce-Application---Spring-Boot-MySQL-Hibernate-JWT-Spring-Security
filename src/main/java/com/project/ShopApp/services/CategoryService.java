package com.project.ShopApp.services;

import com.project.ShopApp.dto.request.CategoryRequest;
import com.project.ShopApp.dto.respone.CreateCategoryResponse;
import com.project.ShopApp.models.Category;
import com.project.ShopApp.exception.ErrorResponse;

import java.util.List;

public interface CategoryService {

    CreateCategoryResponse createCategory(CategoryRequest categoryRequest) throws ErrorResponse;

    List<Category> getAllCategories();

    Category updateCategory(CategoryRequest categoryRequest);
    Category getCategoryById(Long categoryId);

    void deleteCategory(Long categoryId);

}

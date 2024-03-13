package com.project.ShopApp.services.impl;

import com.project.ShopApp.dto.request.CategoryRequest;
import com.project.ShopApp.dto.respone.CreateCategoryResponse;
import com.project.ShopApp.models.Category;
import com.project.ShopApp.repositories.CategoryRepository;
import com.project.ShopApp.exception.ErrorResponse;
import com.project.ShopApp.exception.ErrorResult;
import com.project.ShopApp.exception.SuccessResponse;
import com.project.ShopApp.exception.SuccessResult;
import com.project.ShopApp.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;


    @Override
    public CreateCategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category existingCategory = categoryRepository.findCategoryByName(categoryRequest.getName());
        CreateCategoryResponse createCategoryResponse = new CreateCategoryResponse();

        if (existingCategory!=null){
            throw new ErrorResponse(ErrorResult.DATA_EXISTING);
        }

        Category newCategory = Category.builder()
                .name(categoryRequest.getName())
                .build();

        createCategoryResponse.setData(categoryRepository.save(newCategory));
        createCategoryResponse.setMessage(new SuccessResponse(SuccessResult.CREATED_SUCCESS).getMessage());

        return createCategoryResponse;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(CategoryRequest categoryRequest) {
        Category existingCategory = getCategoryById(categoryRequest.getId());
        if (existingCategory==null){
            throw new ErrorResponse(ErrorResult.NOT_FOUND_DATA);
        }
        existingCategory.setName(categoryRequest.getName());
        categoryRepository.saveAndFlush(existingCategory);
        return existingCategory;
    }
    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new ErrorResponse(ErrorResult.NOT_FOUND_DATA));
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.findById(categoryId).orElseThrow(() -> new ErrorResponse(ErrorResult.NOT_FOUND_DATA));
        categoryRepository.deleteById(categoryId);
    }
}

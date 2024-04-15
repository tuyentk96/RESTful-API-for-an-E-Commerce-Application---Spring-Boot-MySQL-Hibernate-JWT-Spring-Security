package com.project.ShopApp.controllers;

import com.project.ShopApp.dto.request.CategoryRequest;
import com.project.ShopApp.dto.respone.CreateCategoryResponse;
import com.project.ShopApp.dto.respone.ValidatedErrorResponse;
import com.project.ShopApp.exception.SuccessResponse;
import com.project.ShopApp.exception.SuccessResult;
import com.project.ShopApp.models.Category;
import com.project.ShopApp.exception.ErrorResponse;
import com.project.ShopApp.exception.ErrorResult;
import com.project.ShopApp.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryRequest categoryRequest, BindingResult result){

        if (result.hasErrors()){
           return ValidatedErrorResponse.validatedErrorResponse(result);
        }
        return ResponseEntity.ok(categoryService.createCategory(categoryRequest));
    }

    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{category-id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("category-id") Long categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    @PutMapping("")
    public ResponseEntity<?> updateCategory(@Valid @RequestBody CategoryRequest categoryRequest,
                                                          BindingResult result){
        if (result.hasErrors()){
            return ValidatedErrorResponse.validatedErrorResponse(result);
        }
        return ResponseEntity.ok(new SuccessResponse(SuccessResult.UPDATE_SUCCESS,categoryService.updateCategory(categoryRequest)));
    }

    @DeleteMapping("/{category-id}")
    public ResponseEntity<SuccessResponse> deleteCategory(@PathVariable("category-id") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(new SuccessResponse(SuccessResult.DELETE_SUCCESS));
    }

}

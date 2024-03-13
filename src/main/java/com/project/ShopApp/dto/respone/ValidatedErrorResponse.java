package com.project.ShopApp.dto.respone;

import com.project.ShopApp.exception.ErrorResponse;
import com.project.ShopApp.exception.ErrorResult;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ValidatedErrorResponse {
    public static ResponseEntity<List<String>> validatedErrorResponse(BindingResult result) {
        List<String> errorMsg = result.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        return ResponseEntity.badRequest().body(errorMsg);
    }
}

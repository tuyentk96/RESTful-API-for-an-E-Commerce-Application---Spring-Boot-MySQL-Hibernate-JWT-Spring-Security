package com.project.ShopApp.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ErrorResponse.class)
    public ResponseEntity<CustomErrorResponse> handlerException(ErrorResponse ex){
        CustomErrorResponse customErrorResponse = new CustomErrorResponse(ex.getStatus(), ex.getMessage());
        return new ResponseEntity<>(customErrorResponse,HttpStatus.valueOf(ex.getStatus()));
    }
}

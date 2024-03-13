package com.project.ShopApp.exception;

import lombok.Getter;

@Getter
public enum SuccessResult {

    CREATED_SUCCESS(200,"Created data successfully"),
    UPDATE_SUCCESS(200,"Updated data successfully"),
    DELETE_SUCCESS(200,"Delete data successfully");
    private final int status;
    private final String message;

    SuccessResult(int status, String message) {
        this.status = status;
        this.message = message;
    }
}

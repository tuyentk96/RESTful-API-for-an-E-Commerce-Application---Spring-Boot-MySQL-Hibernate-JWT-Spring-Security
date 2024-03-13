package com.project.ShopApp.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ErrorResponse extends RuntimeException {
    private int status;
    private String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorResponse(final ErrorResult result) {
        super();
        this.status = result.getStatus();
        this.message = result.getMessage();
    }
}

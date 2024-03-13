package com.project.ShopApp.exception;

import lombok.Getter;

@Getter
public class SuccessResponse {
    private int status;
    private String message;
    private Object data;

    public SuccessResponse(final SuccessResult result, Object data) {
        this.data = data;
        this.status = result.getStatus();
        this.message = result.getMessage();
    }
    public SuccessResponse(final SuccessResult result) {
        this.status = result.getStatus();
        this.message = result.getMessage();
    }
}

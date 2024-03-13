package com.project.ShopApp.exception;

import lombok.Data;
import lombok.Getter;

@Getter
public enum ErrorResult {
    DATA_EXISTING(409,"Data is existing"),
    PHONE_NUMBER_EXISTING(409,"Phone number already exists"),
    NAME_DATA_EXISTING(409,"Name data is existing"),
    FAILED_VALIDATION_DATA(422, "Failed validation for provided data"),
    NOT_FOUND_DATA(404,"Not found data"),
    NOT_FOUND_USER(404,"Not found user"),
    JWT_CANT_BE_GENERATED(400,"Can't generate token"),
    PASSWORD_NOT_MATCH(404,"Password does not match"),
    INVALID_USER(404,"Invalid phone number or password"),
    NOT_FOUND_ROLE(404,"Not found role"),
    PAYLOAD_TOO_LARGE(413,"File is too large! Maxium size is 10MB"),
    UNSUPPORTED_MEDIA_TYPE(415,"file must be an image"),
    NUMBER_OF_ITEMS_LIMIT(413,"Number of items exceeds the limit"),
    DATA_CANT_BE_EMPTY(400, "Data can't be empty" ),
    DATE_ERROR(400,"Date must be at least today !");

    private final int status;
    private final String message;

    ErrorResult(int status, String message) {
        this.status = status;
        this.message = message;
    }
}

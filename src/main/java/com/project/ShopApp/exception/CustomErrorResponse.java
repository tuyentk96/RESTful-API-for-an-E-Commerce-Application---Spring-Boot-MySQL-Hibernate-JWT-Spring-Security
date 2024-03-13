package com.project.ShopApp.exception;

import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomErrorResponse {
    private int status;
    private String message;
}

package com.project.ShopApp.dto.respone;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Data
@NoArgsConstructor
public class UpdateProductResponse {
    private String message;
    private Object data;
}

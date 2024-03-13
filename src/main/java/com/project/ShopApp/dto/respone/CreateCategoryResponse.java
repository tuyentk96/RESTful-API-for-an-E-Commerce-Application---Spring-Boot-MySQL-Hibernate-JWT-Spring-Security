package com.project.ShopApp.dto.respone;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
public class CreateCategoryResponse {
    private String message;
    private Object data;

    public CreateCategoryResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

}

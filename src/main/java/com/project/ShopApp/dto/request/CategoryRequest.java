package com.project.ShopApp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CategoryRequest {
    private Long id;
    @NotBlank(message = "category name can't be blank")
    private String name;

}

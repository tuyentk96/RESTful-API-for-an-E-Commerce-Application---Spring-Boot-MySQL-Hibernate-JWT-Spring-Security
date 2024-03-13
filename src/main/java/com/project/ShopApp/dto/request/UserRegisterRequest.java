package com.project.ShopApp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserRegisterRequest {
    @JsonProperty("fullname")
    private String fullName;
    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    private String address;
    @NotBlank(message = "Password cannot be blank")
    private String password;
    @JsonProperty("retype_password")
    private String retypePassword;
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;
    @JsonProperty("facebook_account_id")
    private Long facebookAccountId;
    @JsonProperty("google_account_id")
    private Long googleAccountId;
    @JsonProperty("role_id")
    private Long roleId;
}

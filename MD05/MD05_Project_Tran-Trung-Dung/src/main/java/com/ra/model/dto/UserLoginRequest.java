package com.ra.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserLoginRequest {
    @NotBlank(message = "Địa chỉ Email không được để trống")
    @Size(max = 100, message = "Địa chỉ Email phải có độ dài dưới 100 ký tự")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}

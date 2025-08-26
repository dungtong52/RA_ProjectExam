package com.ra.model.dto;

import com.ra.model.entity.Role;
import com.ra.validator.EmailUnique;
import com.ra.validator.PhoneUnique;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PhoneUnique
@EmailUnique
public class UserRegisterRequest {
    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 100, message = "Họ tên phải có độ dài dưới 100 ký tự")
    private String fullName;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate dob;

    @NotBlank(message = "Địa chỉ Email không được để trống")
    @Size(max = 100, message = "Địa chỉ Email phải có độ dài dưới 100 ký tự")
    private String email;

    @NotNull(message = "Giới tính không được để trống")
    private Boolean sex;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Size(max = 20, message = "Số điện thoại phải có độ dài dưới 20 ký tự")
    private String phone;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    private Role role;
}

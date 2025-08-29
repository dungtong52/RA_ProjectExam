package com.ra.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRegisterRequest {
    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 100, message = "Họ tên phải có độ dài dưới 100 ký tự")
    private String name;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate dob;

    @NotBlank(message = "Địa chỉ Email không được để trống")
    @Size(max = 100, message = "Địa chỉ Email phải có độ dài dưới 100 ký tự")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotNull(message = "Giới tính không được để trống")
    private Boolean sex;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Size(max = 20, message = "Số điện thoại phải có độ dài dưới 20 ký tự")
    @Pattern(regexp = "^0\\d{9,10}$", message = "Số điện thoại không đúng định dạng")
    private String phone;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}

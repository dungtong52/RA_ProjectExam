package com.ra.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Author {
    private int id;

    @NotBlank(message = "Tên không được để trống")
    @Size(max = 70, message = "Tên có độ dài nhỏ hơn 70 ký tự")
    private String name;

    @NotNull(message = "Ngày sinh không được để trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 255, message = "Địa chỉ có độ dài nhỏ hơn 255 ký tự")
    private String address;

    @NotBlank(message = "Lĩnh vực không được để trống")
    @Size(max = 50, message = "Lĩnh vực có độ dài nhỏ hơn 50 ký tự")
    private String authorField;

    private boolean status;
}

package com.ra.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Book {
    private int id;

    @NotBlank(message = "Tên không được để trống")
    @Size(max = 100, message = "Tên có độ dài nhỏ hơn 100 ký tự")
    private String name;

    @NotNull(message = "Năm xuất bản không được để trống")
    @Min(value = 1700, message = "Năm xuất bản phải >= 1700")
    private Integer yearPublic;

    @NotNull(message = "Số trang không được để trống")
    @Min(value = 1, message = "Số trang phải lớn hơn 0")
    private Integer pages;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.00", inclusive = false, message = "Giá phải lớn hơn 0")
    private Double price;

    @NotNull(message = "Mã tác giả không được để trống")
    @Min(value = 1, message = "Mã tác giả phải là số nguyên lớn hơn 0")
    private Integer authorId;

    @NotBlank(message = "Đường dẫn ảnh không được để trống")
    @Size(max = 255, message = "Đường dẫn ảnh có độ dài nhỏ hơn 255 ký tự")
    private String bookImage;

    private boolean status;
}

package com.ra.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "courses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    @NotBlank(message = "Tên khóa học không được để trống")
    @Size(max = 100, message = "Tên khóa học phải có độ dài dưới 100 ký tự")
    private String name;

    @Column(name = "duration", nullable = false, columnDefinition = "int check (duration > 0)")
    @NotNull(message = "Thời lượng không được để trống")
    @Positive(message = "Thời lượng phải lớn hơn 0")
    private Integer duration;

    @Column(name = "instructor", nullable = false, length = 100)
    @NotBlank(message = "Giảng viên phụ trách không được để trống")
    @Size(max = 100, message = "Giảng viên phụ trách phải có độ dài dưới 100 ký tự")
    private String instructor;

    @Column(name = "create_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createAt = LocalDate.now();

    @Column(name = "image", length = 500)
    private String image;

    @Column(name = "status", nullable = false)
    @NotNull(message = "Trạng thái không được để trống")
    private Boolean status = true;
}

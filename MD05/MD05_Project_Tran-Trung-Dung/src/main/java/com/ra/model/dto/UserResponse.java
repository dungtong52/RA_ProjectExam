package com.ra.model.dto;

import com.ra.model.entity.Role;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    private Long id;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    private String email;
    private Boolean sex;
    private String phone;
    private LocalDate createAt;
    private Role role;
    private Boolean status;

}

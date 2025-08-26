package com.ra.model.dto;

import com.ra.model.entity.Role;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    private Long id;
    private String fullName;
    private LocalDate dob;
    private String email;
    private Boolean sex;
    private String phone;
    private LocalDate createAt;
    private Role role;
    private Boolean status;
}

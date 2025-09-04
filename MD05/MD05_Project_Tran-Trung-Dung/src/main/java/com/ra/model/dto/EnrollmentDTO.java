package com.ra.model.dto;

import com.ra.model.entity.Course;
import com.ra.model.entity.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EnrollmentDTO {
    private Long id;
    private Course course;
    private EnrollmentStatus status;
}

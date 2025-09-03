package com.ra.model.dto;

import com.ra.model.entity.Course;
import com.ra.model.entity.Enrollment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseDTO {
    private Course course;
    private boolean registered;
}

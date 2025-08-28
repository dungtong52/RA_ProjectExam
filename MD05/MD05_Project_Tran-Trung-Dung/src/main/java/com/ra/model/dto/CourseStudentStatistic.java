package com.ra.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseStudentStatistic {
    private Long courseId;
    private String courseName;
    private Integer duration;
    private String image;
    private Long studentCount;
}

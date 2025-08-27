package com.ra.service;

import com.ra.model.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CourseService {
    Course createCourse(Course course);

    Course updateCourse(Long id, Course course);

    void deleteCourse(Long id);

    Optional<Course> findCourseById(Long id);

    Page<Course> searchCourse(String keyword, Pageable pageable);
}

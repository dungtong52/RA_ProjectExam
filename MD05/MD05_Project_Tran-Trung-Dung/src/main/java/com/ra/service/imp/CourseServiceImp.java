package com.ra.service.imp;

import com.ra.model.entity.Course;
import com.ra.repo.CourseRepo;
import com.ra.service.CourseService;
import com.ra.specification.CourseSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseServiceImp implements CourseService {
    private final CourseRepo courseRepo;

    public CourseServiceImp(CourseRepo courseRepo) {
        this.courseRepo = courseRepo;
    }

    @Override
    public Course createCourse(Course course) {
        return courseRepo.save(course);
    }

    @Override
    public Course updateCourse(Long id, Course course) {
        return findCourseById(id)
                .map(c -> {
                    c.setName(course.getName());
                    c.setDuration(course.getDuration());
                    c.setInstructor(course.getInstructor());
                    c.setCreateAt(course.getCreateAt());
                    c.setImage(course.getImage());
                    return courseRepo.save(c);
                })
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học"));
    }

    @Override
    public void deleteCourse(Long id) {
        findCourseById(id)
                .map(course -> {
                    course.setStatus(false);
                    return courseRepo.save(course);
                })
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học"));
    }

    @Override
    public Optional<Course> findCourseById(Long id) {
        return courseRepo.findById(id);
    }

    @Override
    public Page<Course> searchCourse(String keyword, Pageable pageable) {
        Specification<Course> specification = Specification.allOf(
                CourseSpecification.hasName(keyword),
                CourseSpecification.hasStatus(true)
        );
        return courseRepo.findAll(specification, pageable);
    }
}

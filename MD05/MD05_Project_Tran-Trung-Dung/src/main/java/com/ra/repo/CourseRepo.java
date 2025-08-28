package com.ra.repo;

import com.ra.model.dto.CourseStudentStatistic;
import com.ra.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepo extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
}

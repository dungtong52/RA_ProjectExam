package com.ra.service;

import com.ra.model.entity.Enrollment;
import com.ra.model.entity.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EnrollmentService {
    Page<Enrollment> searchStudentByEnrollment(String keyword, EnrollmentStatus status, Pageable pageable);

    Optional<Enrollment> findById(Long id);

    void approveEnrollment(Long id);

    void denyEnrollment(Long id);
}

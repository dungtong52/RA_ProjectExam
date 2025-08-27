package com.ra.service.imp;

import com.ra.model.entity.Enrollment;
import com.ra.model.entity.EnrollmentStatus;
import com.ra.repo.EnrollmentRepo;
import com.ra.service.EnrollmentService;
import com.ra.specification.EnrollmentSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnrollmentServiceImp implements EnrollmentService {
    private final EnrollmentRepo enrollmentRepo;

    public EnrollmentServiceImp(EnrollmentRepo enrollmentRepo) {
        this.enrollmentRepo = enrollmentRepo;
    }

    @Override
    public Page<Enrollment> searchStudentByEnrollment(String keyword, EnrollmentStatus status, Pageable pageable) {
        Specification<Enrollment> specification = Specification.allOf(
                EnrollmentSpecification.hasCourseName(keyword),
                EnrollmentSpecification.hasStatus(status)
        );
        return enrollmentRepo.findAll(specification, pageable);
    }

    @Override
    public Optional<Enrollment> findById(Long id) {
        return enrollmentRepo.findById(id);
    }

    @Override
    public void approveEnrollment(Long id) {
        Enrollment enrollment = enrollmentRepo.findByStatusAndId(EnrollmentStatus.WAITING, id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đăng ký khóa học"));
        enrollment.setStatus(EnrollmentStatus.CONFIRM);
        enrollmentRepo.save(enrollment);
    }

    @Override
    public void denyEnrollment(Long id) {
        Enrollment enrollment = enrollmentRepo.findByStatusAndId(EnrollmentStatus.WAITING, id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đăng ký khóa học"));
        enrollment.setStatus(EnrollmentStatus.DENIED);
        enrollmentRepo.save(enrollment);
    }
}

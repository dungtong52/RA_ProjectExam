package com.ra.specification;

import com.ra.model.entity.Course;
import com.ra.model.entity.Enrollment;
import com.ra.model.entity.EnrollmentStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class EnrollmentSpecification {
    public static Specification<Enrollment> hasCourseName(String keyword) {
        return (root, query, cb) ->
        {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }
            Join<Enrollment, Course> courseJoin = root.join("course", JoinType.INNER);
            return cb.like(cb.lower(courseJoin.get("name")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<Enrollment> hasStatus(EnrollmentStatus status) {
        return (root, query, cb) ->
                status == null ? null
                        : cb.equal(root.get("status"), status);
    }
}

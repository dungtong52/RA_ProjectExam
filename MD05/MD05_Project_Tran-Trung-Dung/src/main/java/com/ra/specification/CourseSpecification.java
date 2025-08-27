package com.ra.specification;

import com.ra.model.entity.Course;
import org.springframework.data.jpa.domain.Specification;

public class CourseSpecification {
    public static Specification<Course> hasName(String name) {
        return (root, query, cb) ->
                name == null || name.isEmpty() ? null
                        : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Course> hasStatus(Boolean status) {
        return (root, query, cb) ->
                status == null ? null
                        : cb.equal(root.get("status"), status);
    }
}

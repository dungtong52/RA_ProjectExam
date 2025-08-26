package com.ra.specification;

import com.ra.model.entity.Role;
import com.ra.model.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> hasName(String name) {
        return (root, query, cb) ->
                name == null || name.isEmpty() ? null
                        : cb.like(cb.lower(root.get("full_name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, cb) ->
                email == null || email.isEmpty() ? null
                        : cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<User> hasId(Long id) {
        return (root, query, cb) ->
                id == null ? null : cb.like(root.get("id"), "%" + id + "%");
    }

    public static Specification<User> hasStatus(Boolean status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<User> hasRole() {
        return (root, query, cb) ->
                cb.equal(root.get("role"), Role.STUDENT);
    }
}

package com.ra.specification;

import com.ra.model.entity.Role;
import com.ra.model.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> hasKeyword(String keyword) {
        return (root, query, cb) ->
                keyword == null || keyword.isEmpty() ? null
                        : cb.or(
                        cb.like(root.get("id").as(String.class), "%" + keyword.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("email")), "%" + keyword.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("full_name")), "%" + keyword.toLowerCase() + "%")
                );
    }

    public static Specification<User> hasRole(Role role) {
        return (root, query, cb) ->
                role == null ? null : cb.equal(root.get("role"), role);
    }
}

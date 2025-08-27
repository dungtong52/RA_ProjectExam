package com.ra.service;

import com.ra.model.dto.UserLoginRequest;
import com.ra.model.dto.UserRegisterRequest;
import com.ra.model.dto.UserResponse;
import com.ra.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User register(UserRegisterRequest request);

    Optional<UserResponse> login(UserLoginRequest request);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByPhone(String phone);

    Page<User> searchUsers(String keyword, Pageable pageable);

    User updateStudent(Long id, UserRegisterRequest request);

    User changePassword(Long id, String newPassword);

    void changeStatusStudent(Long id);

    boolean checkConfirmPassword(String password, String confirmPassword);

    boolean checkOldPassword(String password, String passwordInput);
}

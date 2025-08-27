package com.ra.service.imp;


import com.ra.model.dto.UserLoginRequest;
import com.ra.model.dto.UserRegisterRequest;
import com.ra.model.dto.UserResponse;
import com.ra.model.entity.Role;
import com.ra.model.entity.User;
import com.ra.model.mapper.UserMapper;
import com.ra.repo.UserRepo;
import com.ra.service.UserService;
import com.ra.specification.UserSpecification;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    public UserServiceImp(UserRepo userRepo, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
    }

    @Override
    public User register(UserRegisterRequest request) {
        User user = userMapper.toModel(request);
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
        return userRepo.save(user);
    }

    @Override
    public Optional<UserResponse> login(UserLoginRequest request) {
        return userRepo.findByEmail(request.getEmail())
                .filter(user -> BCrypt.checkpw(request.getPassword(), user.getPassword()))
                .map(userMapper::toResponse);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public Optional<User> findUserByPhone(String phone) {
        return userRepo.findByPhone(phone);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public Page<User> searchUsers(String keyword, Pageable pageable) {
        Specification<User> specification = Specification.allOf(
                UserSpecification.hasKeyword(keyword),
                UserSpecification.hasRole(Role.STUDENT)
        );
        return userRepo.findAll(specification, pageable);
    }

    @Override
    public User updateStudent(Long id, UserRegisterRequest request) {
        return userRepo.findById(id)
                .map(user -> {
                    user.setName(request.getFullName());
                    user.setDob(request.getDob());
                    user.setEmail(request.getEmail());
                    user.setSex(request.getSex());
                    user.setPhone(request.getPhone());
                    user.setRole(request.getRole());
                    return userRepo.save(user);
                })
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
    }

    @Override
    public User changePassword(Long id, String newPassword) {
        return userRepo.findById(id)
                .map(user -> {
                    user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));
                    return userRepo.save(user);
                })
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
    }

    @Override
    public void changeStatusStudent(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        user.setStatus(!user.getStatus());
        userRepo.save(user);
    }

    @Override
    public boolean checkConfirmPassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    @Override
    public boolean checkOldPassword(String password, String passwordInput) {
        return password.equals(passwordInput);
    }
}

package com.ra.controller;


import com.ra.model.dto.UserLoginRequest;
import com.ra.model.dto.UserRegisterRequest;
import com.ra.model.dto.UserResponse;
import com.ra.model.entity.Role;
import com.ra.model.entity.User;
import com.ra.model.mapper.UserMapper;
import com.ra.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;

    public AuthController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserRegisterRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserRegisterRequest request,
                           BindingResult result,
                           Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("user", request);
            return "auth/register";
        }
        if (userService.findUserByEmail(request.getEmail()).isPresent()) {
            result.rejectValue("email", "error.user", "Email đã tồn tại!");
            return "auth/register";
        }
        if (userService.findUserByPhone(request.getPhone()).isPresent()) {
            result.rejectValue("phone", "error.user", "Số điện thoại đã tồn tại!");
            return "auth/register";
        }
        User newUser = userService.register(request);
        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công. Hãy đăng nhập!");
        return "redirect:/login";
    }

    @GetMapping(value = {"/", "/login"})
    public String showLoginForm(Model model) {
        model.addAttribute("login", new UserLoginRequest());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("login") UserLoginRequest request,
                        BindingResult result,
                        Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("login", request);
            return "auth/login";
        }
        Optional<User> user = userService.findUserByEmail(request.getEmail());

        if (user.isEmpty()) {
            model.addAttribute("error", "Email không tồn tại.");
            return "auth/login";
        }
        User userLogin = user.get();

        if (!userLogin.getStatus()) {
            model.addAttribute("error", "Tài khoản đã bị khóa. Vui lòng liên hệ admin.");
            return "auth/login";
        }

        if (!BCrypt.checkpw(request.getPassword(), userLogin.getPassword())) {
            model.addAttribute("error", "Mật khẩu không chính xác");
            return "auth/login";
        }

        UserResponse userResponse = userMapper.toResponse(userLogin);
        session.setAttribute("currentUser", userResponse);
        redirectAttributes.addFlashAttribute("success", "Đăng nhập thành công");
        if (userLogin.getRole() == Role.ADMIN) {
            return "redirect:/admin";
        } else return "redirect:/user";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

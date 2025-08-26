package com.ra.controller;


import com.ra.model.dto.UserLoginRequest;
import com.ra.model.dto.UserRegisterRequest;
import com.ra.model.entity.Role;
import com.ra.model.entity.User;
import com.ra.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
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
            model.addAttribute("errors", result.getAllErrors());
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
                        Model model, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("login", request);
            model.addAttribute("errors", result.getAllErrors());
            return "auth/login";
        }
        return userService.login(request)
                .map(response -> {
                    session.setAttribute("currentUser", response);

                    model.addAttribute("success", "Đăng nhập thành công");
                    if (response.getRole() == Role.ADMIN) {
                        return "redirect:/admin";
                    } else return "redirect:/user";

                })
                .orElseGet(() -> {
                    model.addAttribute("errorMsg", "Sai tài khoản hoặc mật khẩu");
                    return "auth/login";
                });
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

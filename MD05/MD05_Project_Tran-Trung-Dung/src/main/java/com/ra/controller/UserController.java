package com.ra.controller;

import com.ra.model.dto.CourseDTO;
import com.ra.model.dto.UserChangePasswordRequest;
import com.ra.model.dto.UserRegisterRequest;
import com.ra.model.dto.UserResponse;
import com.ra.model.entity.Course;
import com.ra.model.entity.Enrollment;
import com.ra.model.entity.EnrollmentStatus;
import com.ra.model.entity.User;
import com.ra.model.mapper.UserMapper;
import com.ra.service.CourseService;
import com.ra.service.EnrollmentService;
import com.ra.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final UserService userService;

    public UserController(CourseService courseService, EnrollmentService enrollmentService, UserService userService) {
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.userService = userService;
    }

    @GetMapping
    public String showAllCourse(@RequestParam(required = false) String keyword,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size,
                                HttpSession session,
                                Model model) {
        UserResponse currentUser = (UserResponse) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        Page<Course> coursePage = courseService.searchCourse(keyword, PageRequest.of(page, size));

        List<CourseDTO> courseDTOList = coursePage.getContent().stream()
                .map(course -> {
                    boolean registered = enrollmentService.existsByUserIdAndCourse(currentUser.getId(), course);
                    return new CourseDTO(course, registered);
                })
                .toList();

        model.addAttribute("courses", courseDTOList);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("keyword", keyword);
        return "user/home";
    }

    @GetMapping("/register")
    public String register(@RequestParam("courseId") Long courseId,
                           HttpSession session) {
        UserResponse currentUser = (UserResponse) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        Course course = courseService.findCourseById(courseId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học"));

        boolean exists = enrollmentService.existsByUserIdAndCourse(currentUser.getId(), course);
        if (!exists) {
            User user = userService.findUserById(currentUser.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

            Enrollment enrollment = Enrollment.builder()
                    .user(user)
                    .course(course)
                    .status(EnrollmentStatus.WAITING)
                    .registeredAt(LocalDateTime.now())
                    .build();
            enrollmentService.createEnrollment(enrollment);
        }
        return "redirect:/user";
    }

    @GetMapping("/profile")
    public String showEditForm(Model model, HttpSession session) {
        UserResponse currentUser = (UserResponse) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", currentUser);
        return "user/profile";
    }

    @PostMapping("/profile")
    public String editForm(@Valid @ModelAttribute("user") UserRegisterRequest request,
                           BindingResult result, HttpSession session,
                           Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("user", request);
            return "user/profile";
        }
        UserResponse currentUser = (UserResponse) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        User updatedUser = userService.updateStudent(currentUser.getId(), request);
        session.setAttribute("currentUser", updatedUser);

        redirectAttributes.addFlashAttribute("success", "Thay đổi thông tin tài khoản thành công");
        return "redirect:/user";
    }

    @GetMapping("/profile/changePassword")
    public String showChangePasswordForm(Model model, HttpSession session) {
        UserResponse currentUser = (UserResponse) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        User user = userService.findUserById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        model.addAttribute("password", user.getPassword());
        return "user/changePassword";
    }

    @PostMapping("/profile/changePassword")
    public String changePassword(@Valid @ModelAttribute("user") UserChangePasswordRequest request,
                                 BindingResult result, HttpSession session, Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "user/changePassword";
        }
        UserResponse currentUser = (UserResponse) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        User user = userService.findUserById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (userService.checkOldPassword(user.getPassword(), request.getOldPassword())) {
            if (userService.checkConfirmPassword(request.getNewPassword(), request.getConfirmPassword())) {
                User updatedUser = userService.changePassword(user.getId(), request.getNewPassword());
                session.setAttribute("currentUser", updatedUser);
                redirectAttributes.addFlashAttribute("successMsg", "Thay đổi mật khẩu thành công");
                return "redirect:/user";
            } else {
                model.addAttribute("errorMsg", "Mật khẩu xác nhận không chính xác");
                return "user/changePassword";
            }
        } else {
            model.addAttribute("errorMsg", "Mật khẩu cũ không chính xác");
            return "user/changePassword";
        }
    }

    @GetMapping("/enrollments")
    public String showHistory(@RequestParam(required = false) String keyword,
                              @RequestParam(defaultValue = "status_asc") String sortParam,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size,
                              HttpSession session,
                              Model model) {
        UserResponse currentUser = (UserResponse) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        String[] sortPart = sortParam.split("_");
        String sortBy = sortPart[0];
        String sortDirection = sortPart[1];

        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Page<Course> coursePage = courseService.searchCourse(keyword, PageRequest.of(page, size, sort));

        List<CourseDTO> courseDTOList = coursePage.getContent().stream()
                .filter(course -> enrollmentService.existsByUserIdAndCourse(currentUser.getId(), course))
                .map(course -> new CourseDTO(course, true))
                .toList();

        model.addAttribute("courseDTOs", courseDTOList);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sortParam);
        return "user/history";
    }

    @GetMapping("/cancelEnrollment/{id}")
    public String cancelEnrollment(@PathVariable("id") Long enrollmentId,
                                   @RequestParam(defaultValue = "0") int page,
                                   RedirectAttributes redirectAttributes) {
        enrollmentService.cancelEnrollment(enrollmentId);
        redirectAttributes.addFlashAttribute("success", "Hủy đơn đăng ký thành công");
        return "redirect:/user?page=" + page;
    }
}

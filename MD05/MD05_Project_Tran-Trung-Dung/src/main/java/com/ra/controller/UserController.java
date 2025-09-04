package com.ra.controller;

import com.ra.model.dto.*;
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
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(CourseService courseService, EnrollmentService enrollmentService, UserService userService, UserMapper userMapper) {
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public String showAllCourse(@RequestParam(required = false) String keyword,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size,
                                HttpSession session,
                                Model model) {
        UserResponse currentUser = (UserResponse) session.getAttribute("currentUser");
        Page<Course> coursePage = courseService.searchCourse(keyword, PageRequest.of(page, size));

        List<CourseDTO> courseDTOList = coursePage.getContent().stream()
                .map(course -> {
                    boolean registered = enrollmentService.existsByUserIdAndCourse(currentUser.getId(), course);
                    return new CourseDTO(course, registered);
                })
                .toList();

        model.addAttribute("courseDTOs", courseDTOList);
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
        model.addAttribute("user", userMapper.toUpdateRequest(currentUser));
        return "user/profile";
    }

    @PostMapping("/profile")
    public String editForm(@Valid @ModelAttribute("user") UserUpdateRequest request,
                           BindingResult result, HttpSession session,
                           Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("user", request);
            return "user/profile";
        }
        UserResponse currentUser = (UserResponse) session.getAttribute("currentUser");

        Optional<User> existsEmailUser = userService.findUserByEmail(request.getEmail());
        if (existsEmailUser.isPresent() && !existsEmailUser.get().getId().equals(currentUser.getId())) {
            result.rejectValue("email", "error.user", "Email đã tồn tại!");
            return "user/profile";
        }
        Optional<User> existsPhoneUser = userService.findUserByPhone(request.getPhone());
        if (existsPhoneUser.isPresent() && !existsPhoneUser.get().getId().equals(currentUser.getId())) {
            result.rejectValue("phone", "error.user", "Số điện thoại đã tồn tại!");
            return "user/profile";
        }

        User updatedUser = userService.updateStudent(currentUser.getId(), request);
        UserResponse response = userMapper.toResponse(updatedUser);
        session.setAttribute("currentUser", response);

        redirectAttributes.addFlashAttribute("success", "Thay đổi thông tin tài khoản thành công");
        return "redirect:/user";
    }

    @GetMapping("/profile/changePassword")
    public String showChangePasswordForm(Model model, HttpSession session) {
        UserResponse currentUser = (UserResponse) session.getAttribute("currentUser");

        User user = userService.findUserById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        model.addAttribute("user", new UserChangePasswordRequest());
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

        User user = userService.findUserById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (userService.checkOldPassword(user.getPassword(), request.getOldPassword())) {
            if (userService.checkConfirmPassword(request.getNewPassword(), request.getConfirmPassword())) {
                User updatedUser = userService.changePassword(user.getId(), request.getNewPassword());
                session.setAttribute("currentUser", updatedUser);
                redirectAttributes.addFlashAttribute("successMsg", "Thay đổi mật khẩu thành công");
                return "redirect:/user";
            } else {
                model.addAttribute("error", "Mật khẩu xác nhận không chính xác");
                return "user/changePassword";
            }
        } else {
            model.addAttribute("error", "Mật khẩu cũ không chính xác");
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

        String[] sortPart = sortParam.split("_");
        String sortBy = sortPart[0];
        String sortDirection = sortPart[1];

        Sort sort1 = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Sort sort2 = sort1.and(Sort.by("duration").descending());

        Page<Enrollment> enrollmentPage = enrollmentService.findByUserId(currentUser.getId(), keyword, PageRequest.of(page, size, sort2));

        List<EnrollmentDTO> enrollmentDTOList = enrollmentPage.getContent().stream()
                .map(enrollment ->
                        new EnrollmentDTO(enrollment.getId(), enrollment.getCourse(), enrollment.getStatus())
                )
                .toList();

        model.addAttribute("enrollmentDTOs", enrollmentDTOList);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", enrollmentPage.getTotalPages());
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
        return "redirect:/user/enrollments?page=" + page;
    }
}

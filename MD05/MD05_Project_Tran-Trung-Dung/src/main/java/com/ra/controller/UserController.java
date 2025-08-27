package com.ra.controller;

import com.ra.model.dto.CourseDTO;
import com.ra.model.dto.UserResponse;
import com.ra.model.entity.Course;
import com.ra.model.entity.Enrollment;
import com.ra.model.entity.EnrollmentStatus;
import com.ra.model.entity.User;
import com.ra.service.CourseService;
import com.ra.service.EnrollmentService;
import com.ra.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}

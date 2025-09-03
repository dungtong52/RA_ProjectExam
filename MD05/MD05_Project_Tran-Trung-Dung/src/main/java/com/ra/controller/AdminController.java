package com.ra.controller;

import com.ra.model.dto.CourseStudentStatistic;
import com.ra.model.dto.StatisticResponse;
import com.ra.model.entity.*;
import com.ra.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final CloudinaryService cloudinaryService;
    private final StatisticService statisticService;

    public AdminController(UserService userService, CourseService courseService, EnrollmentService enrollmentService, CloudinaryService cloudinaryService, StatisticService statisticService) {
        this.userService = userService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.cloudinaryService = cloudinaryService;
        this.statisticService = statisticService;
    }

    /*
     * DASHBOARD (Statistic)
     */
    @GetMapping
    public String dashboard(Model model) {
        StatisticResponse statisticResponse = statisticService.getStatistic();
        List<CourseStudentStatistic> courseStudentStatistics = statisticService.getListCourseAndTotalStudent();
        List<CourseStudentStatistic> top5Course = courseStudentStatistics.stream().limit(5).toList();

        model.addAttribute("statistic", statisticResponse);
        model.addAttribute("courses", courseStudentStatistics);
        model.addAttribute("coursesTop", top5Course);
        return "admin/dashboard";
    }

    /*
     * STUDENT MANAGEMENT
     */
    @GetMapping("/users")
    public String getListStudent(@RequestParam(required = false) String keyword,
                                 @RequestParam(defaultValue = "id_asc") String sortParam,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "6") int size,
                                 Model model) {
        String[] sortPart = sortParam.split("_");
        String sortBy = sortPart[0];
        String sortDirection = sortPart[1];

        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
        Page<User> userPage = userService.searchUsers(keyword, PageRequest.of(page, size, sort));
        model.addAttribute("students", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sortParam);
        return "admin/listStudent";
    }

    @GetMapping("/user/{id}")
    public String lockAccount(@PathVariable("id") Long studentId,
                              @RequestParam(defaultValue = "0") int page,
                              RedirectAttributes redirectAttributes) {
        enrollmentService.cancelEnrollmentByStudentId(studentId);
        userService.lockStudent(studentId);
        redirectAttributes.addFlashAttribute("success", "Thay đổi trạng thái tài khoản thành công.\n");
        return "redirect:/admin/users?page=" + page;
    }

    /*
     * COURSE MANAGEMENT
     */
    @GetMapping("/courses")
    public String getListCourse(@RequestParam(required = false) String keyword,
                                @RequestParam(defaultValue = "id_asc") String sortParam,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "6") int size,
                                Model model) {
        String[] sortPart = sortParam.split("_");
        String sortBy = sortPart[0];
        String sortDirection = sortPart[1];

        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
        Page<Course> coursePage = courseService.searchCourse(keyword, PageRequest.of(page, size, sort));
        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sortParam);
        return "admin/listCourse";
    }

    @GetMapping("/course/add")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "admin/addCourse";
    }

    @PostMapping("/course/add")
    public String addCourse(@Valid @ModelAttribute("course") Course course,
                            BindingResult result,
                            @RequestParam("imageFile") MultipartFile uploadFile,
                            Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("course", course);
            return "admin/addCourse";
        }
        try {
            if (!uploadFile.isEmpty()) {
                String imageUrl = cloudinaryService.uploadFile(uploadFile);
                course.setImage(imageUrl);
            }
        } catch (IOException e) {
            model.addAttribute("error", "Có lỗi khi upload file");
            return "admin/addCourse";
        }

        Course newCourse = courseService.createCourse(course);
        redirectAttributes.addFlashAttribute("success", "Thêm khóa học thành công");
        return "redirect:/admin/courses";
    }

    @GetMapping("/course/edit/{id}")
    public String showEditCourseForm(@PathVariable("id") Long id,
                                     Model model) {
        Course editCourse = courseService.findCourseById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học"));
        model.addAttribute("course", editCourse);
        return "admin/editCourse";
    }

    @PostMapping("/course/edit")
    public String editCourse(@Valid @ModelAttribute("course") Course course,
                             BindingResult result,
                             @RequestParam("imageFile") MultipartFile uploadFile,
                             @RequestParam(defaultValue = "0") int page,
                             Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("course", course);
            return "admin/editCourse";
        }
        try {
            Course existingCourse = courseService.findCourseById(course.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học"));

            if (!uploadFile.isEmpty()) {
                String imageUrl = cloudinaryService.uploadFile(uploadFile);
                existingCourse.setImage(imageUrl);
            }
            Course updateCourse = courseService.updateCourse(course.getId(), course);
            redirectAttributes.addFlashAttribute("success", "Cập nhật khóa học thành công");
            return "redirect:/admin/courses?page=" + page;
        } catch (IOException e) {
            model.addAttribute("error", "Có lỗi khi upload file");
            return "admin/editCourse";
        }
    }

    @GetMapping("/course/delete/{id}")
    public String deleteCourse(@PathVariable("id") Long id,
                               @RequestParam(defaultValue = "0") int page,
                               RedirectAttributes redirectAttributes) {
        return courseService.findCourseById(id)
                .map(course -> {
                    courseService.deleteCourse(id);
                    redirectAttributes.addFlashAttribute("success", "Xóa khóa học thành công");
                    return "redirect:/admin/courses?page=" + page;
                })
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học"));
    }

    /*
     * ENROLLMENT MANAGEMENT
     */
    @GetMapping("/enrollments")
    public String getListStudentByEnrollment(@RequestParam(required = false) String keyword,
                                             @RequestParam(required = false) EnrollmentStatus statusFilter,
                                             @RequestParam(defaultValue = "id_asc") String sortParam,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "6") int size,
                                             Model model) {
        String[] sortPart = sortParam.split("_");
        String sortBy = sortPart[0];
        String sortDirection = sortPart[1];

        if ("name".equals(sortBy)) {
            sortBy = "course.name";
        }

        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
        Page<Enrollment> enrollmentPage = enrollmentService.searchStudentByEnrollment(keyword, statusFilter, PageRequest.of(page, size, sort));
        model.addAttribute("enrollments", enrollmentPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", enrollmentPage.getTotalPages());
        model.addAttribute("statusFilter", statusFilter);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sortParam);
        return "admin/listEnrollment";
    }

    @GetMapping("/enrollment/confirm/{id}")
    public String approveEnrollment(@PathVariable("id") Long id,
                                    @RequestParam(defaultValue = "0") int page,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        Enrollment enrollment = enrollmentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đăng ký khóa học"));
        User student = userService.findUserById(enrollment.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));
        if(!student.getStatus()){
            model.addAttribute("error", "Tài khoản sinh viên đang bị khóa, không thể duyệt đơn đăng ký này.");
            return "admin/listEnrollment";
        }
        enrollmentService.approveEnrollment(id);
        redirectAttributes.addFlashAttribute("success", "Duyệt đơn đăng ký thành công");
        return "redirect:/admin/enrollments?page=" + page;
    }

    @GetMapping("/enrollment/deny/{id}")
    public String denyEnrollment(@PathVariable("id") Long id,
                                 @RequestParam(defaultValue = "0") int page,
                                 RedirectAttributes redirectAttributes) {
        enrollmentService.denyEnrollment(id);
        redirectAttributes.addFlashAttribute("success", "Từ chối đơn đăng ký thành công");
        return "redirect:/admin/enrollments?page=" + page;
    }
}

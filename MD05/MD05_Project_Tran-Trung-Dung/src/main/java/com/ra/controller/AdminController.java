package com.ra.controller;

import com.ra.model.entity.Course;
import com.ra.model.entity.Enrollment;
import com.ra.model.entity.EnrollmentStatus;
import com.ra.model.entity.User;
import com.ra.service.CloudinaryService;
import com.ra.service.CourseService;
import com.ra.service.EnrollmentService;
import com.ra.service.UserService;
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

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final CloudinaryService cloudinaryService;

    public AdminController(UserService userService, CourseService courseService, EnrollmentService enrollmentService, CloudinaryService cloudinaryService) {
        this.userService = userService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.cloudinaryService = cloudinaryService;
    }

    /*
     * DASHBOARD
     */
//    @GetMapping("/dashboard")
//    public String dashboard()


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
    public String changeStatusAccount(@PathVariable("id") Long id,
                                      @RequestParam(defaultValue = "0") int page,
                                      RedirectAttributes redirectAttributes) {
        userService.changeStatusStudent(id);
        redirectAttributes.addFlashAttribute("success", "Thay đổi trạng thái tài khoản thành công.");
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
            String imageUrl = cloudinaryService.uploadFile(uploadFile);
            course.setImage(imageUrl);
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
            String imageUrl = cloudinaryService.uploadFile(uploadFile);
            course.setImage(imageUrl);
        } catch (IOException e) {
            model.addAttribute("error", "Có lỗi khi upload file");
            return "admin/editCourse";
        }
        Course updateCourse = courseService.updateCourse(course.getId(), course);
        redirectAttributes.addFlashAttribute("success", "Cập nhật khóa học thành công");
        return "redirect:/admin/courses?page=" + page;
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
                                    RedirectAttributes redirectAttributes) {
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

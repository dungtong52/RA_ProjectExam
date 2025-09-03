package com.ra.service.imp;

import com.ra.model.dto.CourseStudentStatistic;
import com.ra.model.dto.StatisticResponse;
import com.ra.model.entity.Role;
import com.ra.repo.CourseRepo;
import com.ra.repo.EnrollmentRepo;
import com.ra.repo.UserRepo;
import com.ra.service.StatisticService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticServiceImp implements StatisticService {
    private final UserRepo userRepo;
    private final CourseRepo courseRepo;
    private final EnrollmentRepo enrollmentRepo;

    public StatisticServiceImp(UserRepo userRepo, CourseRepo courseRepo, EnrollmentRepo enrollmentRepo) {
        this.userRepo = userRepo;
        this.courseRepo = courseRepo;
        this.enrollmentRepo = enrollmentRepo;
    }


    @Override
    public StatisticResponse getStatistic() {
        StatisticResponse statistic = new StatisticResponse();
        statistic.setTotalStudent(userRepo.countByRole(Role.STUDENT));
        statistic.setTotalCourse(courseRepo.countByStatus(true));
        statistic.setTotalEnrollment(enrollmentRepo.count());
        return statistic;
    }

    @Override
    public List<CourseStudentStatistic> getListCourseAndTotalStudent() {
        return enrollmentRepo.getCourseWithTotalStudent();
    }
}

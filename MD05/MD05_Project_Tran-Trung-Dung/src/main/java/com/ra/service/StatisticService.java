package com.ra.service;

import com.ra.model.dto.CourseStudentStatistic;
import com.ra.model.dto.StatisticResponse;

import java.util.List;

public interface StatisticService {
    StatisticResponse getStatistic();

    List<CourseStudentStatistic> getListCourseAndTotalStudent();
}

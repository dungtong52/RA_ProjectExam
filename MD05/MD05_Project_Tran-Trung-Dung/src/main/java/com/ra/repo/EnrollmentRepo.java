package com.ra.repo;

import com.ra.model.dto.CourseStudentStatistic;
import com.ra.model.entity.Course;
import com.ra.model.entity.Enrollment;
import com.ra.model.entity.EnrollmentStatus;
import com.ra.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepo extends JpaRepository<Enrollment, Long>, JpaSpecificationExecutor<Enrollment> {

    Optional<Enrollment> findByStatusAndId(EnrollmentStatus status, Long id);

    boolean existsByUserIdAndCourse(Long userId, Course course);

    @Query(value = """
                select new com.ra.model.dto.CourseStudentStatistic(
                c.id, c.name, c.duration, c.image, count(distinct e.user.id)
                )
                from Enrollment e
                join e.course c
                group by c.id, c.name, c.duration, c.image
                order by c.id asc
            """)
    List<CourseStudentStatistic> getCourseWithTotalStudent();

    List<Enrollment> findByUser(User user);

    List<Enrollment> findByUser_Id(Long userId);

    List<Enrollment> findByStatusAndUser_Id(EnrollmentStatus status, Long userId);
}

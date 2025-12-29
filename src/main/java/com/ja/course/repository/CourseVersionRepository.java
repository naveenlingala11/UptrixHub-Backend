package com.ja.course.repository;

import com.ja.course.entity.CourseVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseVersionRepository
        extends JpaRepository<CourseVersion, Long> {

    Optional<CourseVersion> findByCourseIdAndVersion(
            String courseId,
            Integer version
    );

    List<CourseVersion> findByCourseIdOrderByVersionDesc(
            String courseId
    );
}

    package com.ja.course.dto;

    import lombok.Data;

    @Data
    public class CourseRequest {
        private String courseId;
        private String title;
        private String jsonData; // full course JSON
    }

package com.ja.course.player;

import lombok.Data;
import java.util.List;

@Data
public class CoursePlayerData {

    private String courseId;
    private String title;
    private List<Chapter> chapters;

    @Data
    public static class Chapter {
        private String chapterId;
        private String title;
        private List<Section> sections;
    }

    @Data
    public static class Section {
        private String sectionId;
        private String heading;
        private List<Object> content; // paragraph, code, image, list, etc
    }
}

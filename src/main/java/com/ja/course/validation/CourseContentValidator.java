package com.ja.course.validation;

import com.ja.course.player.CoursePlayerData;

public class CourseContentValidator {

    public static void validate(CoursePlayerData data) {

        if (data == null) {
            throw new IllegalArgumentException("Content is empty");
        }

        if (data.getChapters() == null || data.getChapters().isEmpty()) {
            throw new IllegalArgumentException("Chapters are required");
        }

        data.getChapters().forEach(ch -> {
            if (ch.getSections() == null || ch.getSections().isEmpty()) {
                throw new IllegalArgumentException(
                        "Chapter '" + ch.getTitle() + "' has no sections"
                );
            }

            ch.getSections().forEach(sec -> {
                if (sec.getContent() == null || sec.getContent().isEmpty()) {
                    throw new IllegalArgumentException(
                            "Section '" + sec.getHeading() + "' has no content"
                    );
                }
            });
        });
    }
}

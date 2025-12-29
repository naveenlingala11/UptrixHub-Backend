package com.ja.course.controller;

import com.ja.course.dto.CourseCardDto;
import com.ja.course.service.MyCoursesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/courses")
@RequiredArgsConstructor
public class MyCoursesController {

    private final MyCoursesService myCoursesService;

    @GetMapping("/my")
    public List<CourseCardDto> myCourses(
            @AuthenticationPrincipal UserDetails principal
    ) {
        Long userId = Long.valueOf(principal.getUsername());
        return myCoursesService.getMyCourses(userId);
    }
}

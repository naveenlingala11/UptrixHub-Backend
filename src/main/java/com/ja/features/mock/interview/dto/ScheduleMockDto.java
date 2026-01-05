package com.ja.features.mock.interview.dto;

import java.time.LocalDateTime;

public record ScheduleMockDto(
        Long requestId,
        Long interviewerId,
        LocalDateTime time,
        int duration,
        String meetingLink
) {}

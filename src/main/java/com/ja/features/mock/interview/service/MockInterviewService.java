package com.ja.features.mock.interview.service;

import com.ja.features.mock.interview.dto.MockInterviewRequestDto;
import com.ja.features.mock.interview.dto.MockInterviewSessionDto;
import com.ja.features.mock.interview.dto.ReviewMockDto;
import com.ja.features.mock.interview.dto.ScheduleMockDto;
import com.ja.features.mock.interview.entity.MockInterviewRequest;
import com.ja.features.mock.interview.entity.MockInterviewSession;
import com.ja.features.mock.interview.repository.MockInterviewRequestRepository;
import com.ja.features.mock.interview.repository.MockInterviewSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MockInterviewService {

    private final MockInterviewRequestRepository requestRepo;
    private final MockInterviewSessionRepository sessionRepo;

    /* ================= CANDIDATE ================= */

    public void requestMock(Long userId, MockInterviewRequestDto dto) {

        requestRepo.save(
                MockInterviewRequest.builder()
                        .userId(userId)
                        .role(dto.role())
                        .preferredSlots(dto.preferredSlots())
                        .platform(dto.platform())
                        .status("REQUESTED")
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    @Transactional(readOnly = true) // 🔥 THIS FIXES EVERYTHING
    public List<MockInterviewRequestDto> myRequests(Long userId) {

        return requestRepo.findWithSlotsByUserId(userId)
                .stream()
                .map(r -> new MockInterviewRequestDto(
                        r.getId(),
                        r.getRole(),
                        r.getPlatform(),
                        r.getStatus(),
                        r.getPreferredSlots(), // ✅ already loaded
                        r.getCreatedAt()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MockInterviewSessionDto> mySessions(Long userId) {

        var requestIds = requestRepo.findByUserId(userId)
                .stream()
                .map(MockInterviewRequest::getId)
                .toList();

        return sessionRepo.findByRequestIdIn(requestIds)
                .stream()
                .map(s -> new MockInterviewSessionDto(
                        s.getId(),
                        s.getRequestId().toString(), // or role if you add join later
                        s.getStatus(),
                        s.getScheduledAt(),
                        s.getMeetingLink(),
                        s.getScore(),
                        s.getStrengths(), // ✅ safe
                        s.getWeaknesses(), // ✅ safe
                        s.getReview()
                ))
                .toList();
    }

    /* ================= ADMIN ================= */

    public List<MockInterviewRequest> pendingRequests() {
        return requestRepo.findByStatus("REQUESTED");
    }

    public void schedule(ScheduleMockDto dto) {

        sessionRepo.save(
                MockInterviewSession.builder()
                        .requestId(dto.requestId())
                        .interviewerId(dto.interviewerId())
                        .meetingLink(dto.meetingLink())
                        .scheduledAt(dto.time())
                        .duration(dto.duration())
                        .status("UPCOMING")
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        var req = requestRepo.findById(dto.requestId()).orElseThrow();
        req.setStatus("SCHEDULED");
        requestRepo.save(req);
    }

    public void review(Long sessionId, ReviewMockDto dto) {

        var s = sessionRepo.findById(sessionId).orElseThrow();

        s.setScore(dto.score());
        s.setStrengths(dto.strengths());
        s.setWeaknesses(dto.weaknesses());
        s.setReview(dto.review());
        s.setStatus("COMPLETED");

        sessionRepo.save(s);

        var req = requestRepo.findById(s.getRequestId()).orElseThrow();
        req.setStatus("COMPLETED");
        requestRepo.save(req);
    }
}

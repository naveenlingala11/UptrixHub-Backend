package com.ja.admin.controller;

import com.ja.admin.dto.SubscriptionUpdateRequest;
import com.ja.course.dto.CourseRequest;
import com.ja.course.entity.Course;
import com.ja.course.enums.PriceType;
import com.ja.course.repository.CourseRepository;
import com.ja.user.enums.Role;
import com.ja.user.enums.Subscription;
import com.ja.user.entity.User;
import com.ja.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /* ================= USERS ================= */

    @GetMapping("/users")
    public Page<User> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return userRepository.findAll(PageRequest.of(page, size));
    }

    @PutMapping("/users/{id}/toggle")
    public void toggleUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow();
        if (user.getRole() == Role.ADMIN)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
    }

    @PutMapping("/users/{id}/reset-password")
    public String resetPassword(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow();
        String temp = "Temp@" + new Random().nextInt(9999);
        user.setPassword(passwordEncoder.encode(temp));
        userRepository.save(user);
        return temp;
    }

    @GetMapping("/users/search")
    public List<User> search(@RequestParam String q) {
        return userRepository
                .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseAndDeletedFalse(q, q);
    }
    /* ===================== DELETE USER ===================== */

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow();

        if (user.getRole() == Role.ADMIN) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Cannot delete admin"
            );
        }

        user.setDeleted(true);
        userRepository.save(user);
    }

    /* ===================== SUBSCRIPTION CONTROL ===================== */

    @PutMapping("/users/{id}/subscription")
    public ResponseEntity<?> updateSubscription(
            @PathVariable Long id,
            @RequestBody SubscriptionUpdateRequest req
    ) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"
                ));

        if (req.isEnable()) {
            user.setSubscription(Subscription.PRO);
            user.setSubscriptionActive(true);
            user.setSubscriptionExpiry(
                    LocalDate.now().plusDays(req.getGraceDays())
            );
        } else {
            user.setSubscription(Subscription.FREE);
            user.setSubscriptionActive(false);
            user.setSubscriptionExpiry(null);
        }

        userRepository.save(user);
        return ResponseEntity.ok("Subscription updated");
    }

    @GetMapping("/subscriptions/expiring")
    public List<User> expiringSoon() {
        return userRepository
                .findBySubscriptionExpiryBetweenAndDeletedFalse(
                        LocalDate.now(),
                        LocalDate.now().plusDays(7)
                );
    }

    /* ================= SAVE COURSE ================= */

    @PostMapping("/course")
    public ResponseEntity<?> saveCourse(@RequestBody CourseRequest req) {

        String courseId = req.getCourseId(); // e.g. "core-java"

        if (courseRepository.existsById(courseId)) {
            return ResponseEntity
                    .badRequest()
                    .body("Course already exists");
        }

        Course course = new Course();
        course.setId(courseId);
        course.setTitle(req.getTitle());
        course.setDescription(req.getJsonData()); // or syllabus JSON
        course.setPriceType(PriceType.PRO); // default
        course.setPrice(0);
        course.setActive(true);

        courseRepository.save(course);

        return ResponseEntity.ok("Course saved successfully");
    }


    /* ================= STATS ================= */

    @GetMapping("/stats")
    public Map<String, Long> stats() {
        return Map.of(
                "users", userRepository.count(),
                "proUsers", userRepository.countBySubscriptionAndDeletedFalse(Subscription.PRO)
        );
    }


}

package com.ja.checkout.service;

import com.ja.bundle.entity.Bundle;
import com.ja.bundle.repository.BundleRepository;
import com.ja.checkout.dto.CheckoutItem;
import com.ja.checkout.dto.CheckoutRequest;
import com.ja.checkout.dto.CheckoutResponse;
import com.ja.course.entity.Course;
import com.ja.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CourseRepository courseRepo;
    private final BundleRepository bundleRepo;

    public CheckoutResponse preview(CheckoutRequest req) {

        /* 1️⃣ Collect course IDs (bundle + individual) */
        Set<String> courseIds = new LinkedHashSet<>();

        if (req.bundleId() != null) {
            Bundle bundle = bundleRepo.findById(req.bundleId())
                    .orElseThrow(() -> new RuntimeException("Bundle not found"));
            courseIds.addAll(bundle.getCourseIds());
        }

        if (req.courseIds() != null) {
            courseIds.addAll(req.courseIds());
        }

        /* 2️⃣ Calculate pricing */
        int subtotal = 0;
        List<CheckoutItem> items = new ArrayList<>();

        for (String courseId : courseIds) {

            Course course = courseRepo.findById(courseId)
                    .orElseThrow(() ->
                            new RuntimeException("Course not found: " + courseId));

            if (!course.isActive()) {
                throw new RuntimeException("Course inactive: " + courseId);
            }

            int price = course.getPriceType().name().equals("FREE")
                    ? 0
                    : course.getPrice();

            subtotal += price;

            items.add(new CheckoutItem(
                    course.getId(),
                    course.getTitle(),
                    price
            ));
        }

        /* 3️⃣ GST + Total */
        int gst = (int) Math.round(subtotal * 0.18);
        int total = subtotal + gst;

        return new CheckoutResponse(
                items,
                subtotal,
                0,
                gst,
                total
        );
    }
}

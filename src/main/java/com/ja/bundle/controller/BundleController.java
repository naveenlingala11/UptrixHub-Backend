package com.ja.bundle.controller;

import com.ja.bundle.entity.Bundle;
import com.ja.bundle.repository.BundleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bundles")
@RequiredArgsConstructor
public class BundleController {

    private final BundleRepository bundleRepo;

    @GetMapping
    public List<Bundle> getAll() {
        return bundleRepo.findAll();
    }
}

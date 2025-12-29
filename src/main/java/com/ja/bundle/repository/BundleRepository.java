package com.ja.bundle.repository;

import com.ja.bundle.entity.Bundle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BundleRepository extends JpaRepository<Bundle, Long> {

    Optional<Bundle> findByCode(String code);

    boolean existsByCode(String code);
}

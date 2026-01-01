package com.ja.pseudo.repository;

import com.ja.pseudo.entity.PseudoSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PseudoSkillRepository
        extends JpaRepository<PseudoSkill, Long> {

    Optional<PseudoSkill> findBySlugAndActiveTrue(String slug);

    List<PseudoSkill> findByActiveTrueOrderByTitleAsc();

    List<PseudoSkill> findByActiveTrue();

}


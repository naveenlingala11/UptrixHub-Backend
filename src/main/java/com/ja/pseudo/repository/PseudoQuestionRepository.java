package com.ja.pseudo.repository;

import com.ja.pseudo.entity.PseudoQuestion;
import com.ja.pseudo.entity.PseudoSkill;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PseudoQuestionRepository
        extends JpaRepository<PseudoQuestion, Long> {

    Page<PseudoQuestion> findBySkill(
            PseudoSkill skill,
            Pageable pageable
    );

    @Query("""
        SELECT q FROM PseudoQuestion q
        WHERE q.skill = :skill
        AND (
            q.accessLevel = 'FREE'
            OR (:isPro = true AND q.accessLevel = 'PRO')
        )
    """)
    Page<PseudoQuestion> findBySkillWithAccess(
            @Param("skill") PseudoSkill skill,
            @Param("isPro") boolean isPro,
            Pageable pageable
    );

    List<PseudoQuestion> findAllBySkillOrderByIdAsc(PseudoSkill skill);

}

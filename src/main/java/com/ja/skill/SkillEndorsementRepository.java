package com.ja.skill;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillEndorsementRepository extends JpaRepository<SkillEndorsement, Long> {

    long countBySkillId(Long skillId);
    boolean existsByEndorserIdAndSkillId(Long endorserId, Long skillId);
}

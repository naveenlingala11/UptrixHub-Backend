package com.ja.skill;

import com.ja.user.entity.User;
import com.ja.user.entity.UserSkill;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"endorser_id", "skill_id"}
        )
)
public class SkillEndorsement {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User endorser;

    @ManyToOne
    private UserSkill skill;

    private LocalDateTime createdAt = LocalDateTime.now();

    // ✅ REQUIRED
    public SkillEndorsement() {}

    // ✅ ADD THIS
    public SkillEndorsement(User endorser, UserSkill skill) {
        this.endorser = endorser;
        this.skill = skill;
    }
}

package com.ja.challenge.entity;

import com.ja.challenge.dto.TestCaseDto;
import com.ja.code.dto.TestCase;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor // REQUIRED
@AllArgsConstructor
@Entity
@Table(name = "daily_challenge")
public class DailyChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String challengeDate;

    private String title;
    private String difficulty;
    private String time;

    @ElementCollection
    @CollectionTable(
            name = "daily_challenge_tags",
            joinColumns = @JoinColumn(name = "challenge_id")
    )
    private Set<String> tags = new HashSet<>();

    @ElementCollection
    @CollectionTable(
            name = "daily_challenge_problem",
            joinColumns = @JoinColumn(name = "challenge_id")
    )
    @Column(length = 2000)
    private List<String> problem = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String starterCode;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<TestCaseDto> tests;

    private int xpReward;
}

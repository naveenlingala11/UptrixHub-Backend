package com.ja.bundle.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "bundles")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Bundle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    private String title;

    private int discountedPrice;

    @ElementCollection
    @CollectionTable(
            name = "bundle_course_ids",
            joinColumns = @JoinColumn(name = "bundle_id")
    )
    @Column(name = "course_id")
    private List<String> courseIds;
}

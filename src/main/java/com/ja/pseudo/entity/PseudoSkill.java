package com.ja.pseudo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pseudo_skill")
@Getter
@Setter
public class PseudoSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private boolean active = true;
}

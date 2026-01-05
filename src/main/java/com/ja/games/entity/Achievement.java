package com.ja.games.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "achievement")
@Getter
@Setter
public class Achievement {

    @Id
    @GeneratedValue
    private Long id;

    private String code;
    private String title;
    private String description;
    private String icon;

    private Integer requiredXp;
    private Integer requiredStreak;

    private boolean enabled = true;

}

package com.ja.course.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.ja.course.enums.PriceType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "courses")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @Column(length = 100)
    private String id;

    private String title;
    private String category;
    private String level;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private PriceType priceType;

    private int price;

    @Column(name = "is_active")
    private boolean active = true;

    private Integer contentVersion = 1;
    private boolean published = false;

    /* ================= JSONB ================= */

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode draftContent;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode publishedContent;
}

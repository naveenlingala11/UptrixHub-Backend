package com.ja.admin.events;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @Column(length = 1000)
    private String message;

    private LocalDateTime createdAt = LocalDateTime.now();

    // convenience constructor
    public AdminEvent(String type, String message) {
        this.type = type;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }
}

package com.ja.subscription.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
public class Subscription {

    @Id
    private Long userId;

    private String plan;
    private Instant startDate;
    private Instant endDate;
    private boolean active;
}

package com.ja.user.entity;

import com.ja.user.enums.AuthProvider;
import com.ja.user.enums.Role;
import com.ja.user.enums.Subscription;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String mobile;

    private String password;

    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    // ✅ ADD THESE
    private String avatar;

    @Enumerated(EnumType.STRING)
    private Subscription subscription = Subscription.FREE;

    private String providerId;

    private String otp;

    @Column(nullable = false)
    private boolean deleted = false;

    private LocalDateTime otpExpiry;

    private Boolean subscriptionActive = false;

    private LocalDate subscriptionExpiry; // 👈 GRACE PERIOD SUPPORT

    @Column(length = 500)
    private String bio;   // ✅ NEW
}

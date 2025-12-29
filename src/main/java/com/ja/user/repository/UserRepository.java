package com.ja.user.repository;

import com.ja.user.entity.UserDailyActivity;
import com.ja.user.enums.Subscription;
import com.ja.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /* ================= SAFE AUTH ================= */

    Optional<User> findByEmailAndDeletedFalse(String email);

    boolean existsByEmail(String email);
    boolean existsByMobile(String mobile);

    /* ================= ADMIN ================= */

    List<User> findAllByDeletedFalse();

    Optional<User> findByIdAndDeletedFalse(Long id);

    List<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseAndDeletedFalse(
            String name,
            String email
    );

    Optional<User> findByNameIgnoreCaseAndDeletedFalse(String name);

    /* ================= SUBSCRIPTION ================= */

    Long countBySubscriptionAndDeletedFalse(Subscription subscription);

    List<User> findBySubscriptionExpiryBetweenAndDeletedFalse(
            LocalDate start,
            LocalDate end
    );

}

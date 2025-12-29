package com.ja.admin.events;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminEventRepository
        extends JpaRepository<AdminEvent, Long> {
}

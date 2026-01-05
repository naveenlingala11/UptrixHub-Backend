package com.ja.games.repository;

import com.ja.games.entity.XpTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface XpTransactionRepository
        extends JpaRepository<XpTransaction, Long> {
}

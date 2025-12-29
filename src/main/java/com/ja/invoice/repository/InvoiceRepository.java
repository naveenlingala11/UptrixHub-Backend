package com.ja.invoice.repository;

import com.ja.invoice.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Invoice findByOrderId(Long orderId);

    List<Invoice> findAllByUserId(Long userId);

    Long findUserIdByOrderId(Long orderId);
}

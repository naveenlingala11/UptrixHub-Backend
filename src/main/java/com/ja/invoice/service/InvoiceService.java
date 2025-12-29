package com.ja.invoice.service;

import com.ja.invoice.dto.BillingStats;
import com.ja.invoice.entity.Invoice;
import com.ja.invoice.repository.InvoiceRepository;
import com.ja.order.entity.Order;
import com.ja.order.repository.OrderRepository;
import com.ja.user.entity.User;
import com.ja.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.Year;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoicePdfService pdfService;
    private final UserRepository userRepository;
    private final InvoiceMailService invoiceMailService;
    private final OrderRepository orderRepository;
    /* ================= GENERATE ================= */
    @Transactional
    public Invoice generateInvoice(Long orderId) {

        Invoice existing = invoiceRepository.findByOrderId(orderId);
        if (existing != null) return existing;

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Invoice invoice = Invoice.builder()
                .invoiceNumber(generateInvoiceNumber())
                .orderId(orderId)
                .userId(order.getUserId())   // ✅ CORRECT SOURCE
                .subtotal(order.getSubtotal())
                .gst(order.getGst())
                .total(order.getTotal())
                .generatedAt(Instant.now())
                .build();

        return invoiceRepository.save(invoice);
    }


    /* ================= ZIP ALL ================= */
    public byte[] generateZip(Long userId) {

        try {
            List<Invoice> invoices = invoiceRepository.findAllByUserId(userId);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(baos);

            User user = userRepository.findById(userId).orElseThrow();

            for (Invoice inv : invoices) {
                byte[] pdf = pdfService.generatePdf(inv, user);
                zip.putNextEntry(
                        new ZipEntry(inv.getInvoiceNumber() + ".pdf"));
                zip.write(pdf);
                zip.closeEntry();
            }

            zip.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("ZIP generation failed", e);
        }
    }

    /* ================= EMAIL INVOICE ================= */
    @Async
    public void emailInvoice(Long invoiceId, Long userId) {

        System.out.println("🔥 EMAIL INVOICE METHOD HIT 🔥");

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        byte[] pdf = pdfService.generatePdf(invoice, user);

        invoiceMailService.sendInvoice(user, invoice, pdf);
    }

    /* ================= FETCH ================= */
    public Invoice getByOrderId(Long orderId) {
        return invoiceRepository.findByOrderId(orderId);
    }

    public List<Invoice> getUserInvoices(Long userId) {
        return invoiceRepository.findAllByUserId(userId);
    }

    /* ================= STATS ================= */
    public BillingStats stats(Long userId) {
        List<Invoice> invoices = invoiceRepository.findAllByUserId(userId);

        return new BillingStats(
                invoices.stream().mapToInt(Invoice::getTotal).sum(),
                invoices.size(),
                invoices.stream().mapToInt(Invoice::getGst).sum()
        );
    }

    /* ================= HELPERS ================= */
    private String generateInvoiceNumber() {
        long next = invoiceRepository.count() + 1;
        return String.format("JA-%d-%06d", Year.now().getValue(), next);
    }

    private Long fetchUserId(Long orderId) {
        return invoiceRepository.findUserIdByOrderId(orderId);
    }
}

package com.ja.invoice.controller;

import com.ja.invoice.dto.BillingStats;
import com.ja.invoice.entity.Invoice;
import com.ja.invoice.service.InvoicePdfService;
import com.ja.invoice.service.InvoiceService;
import com.ja.security.JwtUserPrincipal;
import com.ja.security.UserPrincipal;
import com.ja.user.entity.User;
import com.ja.user.repository.UserRepository;
import com.resend.core.exception.ResendException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final InvoicePdfService pdfService;
    private final UserRepository userRepository;

    /* ================= GENERATE ================= */
    @PostMapping("/generate/{orderId}")
    public Invoice generate(
            @PathVariable Long orderId
    ) {
        return invoiceService.generateInvoice(orderId);
    }

    /* ================= FETCH ================= */
    @GetMapping("/order/{orderId}")
    public Invoice byOrder(@PathVariable Long orderId) {
        return invoiceService.getByOrderId(orderId);
    }

    @GetMapping("/my")
    public List<Invoice> myInvoices(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return invoiceService.getUserInvoices(principal.getId());
    }

    @GetMapping(
            value = "/pdf/{orderId}",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> downloadPdf(
            @PathVariable Long orderId,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        if (principal == null) {
            throw new RuntimeException("Unauthenticated access");
        }

        Invoice invoice = invoiceService.getByOrderId(orderId);

        if (!invoice.getUserId().equals(principal.getUserId())) {
            throw new RuntimeException("Unauthorized invoice access");
        }

        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        byte[] pdf = pdfService.generatePdf(invoice, user);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + invoice.getInvoiceNumber() + ".pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    /* ================= STATS ================= */
    @GetMapping("/stats")
    public BillingStats stats(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return invoiceService.stats(principal.getId());
    }

    @GetMapping("/pdf/all")
    public ResponseEntity<byte[]> downloadAll(
            @AuthenticationPrincipal JwtUserPrincipal principal) {

        byte[] zip = invoiceService.generateZip(principal.getUserId());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=invoices.zip")
                .body(zip);
    }

    @PostMapping("/email/{invoiceId}")
    public ResponseEntity<Void> resend(@PathVariable Long invoiceId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof JwtUserPrincipal principal)) {
            throw new RuntimeException("Unauthenticated");
        }

        invoiceService.emailInvoice(invoiceId, principal.getUserId());
        return ResponseEntity.ok().build();
    }

}

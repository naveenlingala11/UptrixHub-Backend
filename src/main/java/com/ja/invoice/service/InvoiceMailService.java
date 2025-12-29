package com.ja.invoice.service;

import com.ja.invoice.entity.Invoice;
import com.ja.user.entity.User;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.Attachment;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceMailService {

    private final Resend resend;

    @Value("${resend.from}")
    private String from;

    public void sendInvoice(User user, Invoice invoice, byte[] pdfBytes) {

        try {
            System.out.println("🔥 RESEND EMAIL METHOD HIT 🔥");

            String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);

            CreateEmailOptions email = CreateEmailOptions.builder()
                    .from(from)
                    .to(user.getEmail())
                    .subject("Your Invoice " + invoice.getInvoiceNumber())
                    .html("""
                    <h2>Thanks for your purchase!</h2>
                    <p>Invoice: <b>%s</b></p>
                    <p>Total: ₹%s</p>
                    <p>— Uptrix Hub</p>
                """.formatted(
                            invoice.getInvoiceNumber(),
                            invoice.getTotal()
                    ))
                    .attachments(List.of(
                            Attachment.builder()
                                    .fileName(invoice.getInvoiceNumber() + ".pdf")
                                    .content(base64Pdf)
                                    .build()
                    ))
                    .build();

            var response = resend.emails().send(email);

            System.out.println("✅ RESEND RESPONSE: " + response.getId());

        } catch (Exception e) {
            e.printStackTrace(); // ← IMPORTANT
            throw new RuntimeException("Invoice email failed", e);
        }
    }

}


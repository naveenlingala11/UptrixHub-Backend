package com.ja.payment.service;

import com.ja.invoice.service.InvoiceService;
import com.ja.order.entity.Order;
import com.ja.order.enums.OrderStatus;
import com.ja.order.repository.OrderRepository;
import com.ja.payment.dto.CreateRazorpayOrderResponse;
import com.ja.payment.dto.RazorpayVerifyRequest;
import com.ja.payment.entity.OrderItem;
import com.ja.payment.entity.Payment;
import com.ja.payment.enums.PaymentStatus;
import com.ja.payment.repository.OrderItemRepository;
import com.ja.payment.repository.PaymentRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RazorpayPaymentService {

    private final RazorpayClient razorpayClient;
    private final PaymentRepository paymentRepo;
    private final OrderRepository orderRepo;
    private final InvoiceService invoiceService;
    private final OrderItemRepository orderItemRepository;

    @Value("${razorpay.key-id}")
    private String keyId;

    @Value("${razorpay.key-secret}")
    private String razorpaySecret;

    /* ================= CREATE RAZORPAY ORDER ================= */
    public CreateRazorpayOrderResponse createOrder(Order appOrder) throws Exception {

        JSONObject request = new JSONObject();
        request.put("amount", appOrder.getTotal() * 100);
        request.put("currency", "INR");
        request.put("receipt", "order_" + appOrder.getId());

        com.razorpay.Order razorpayOrder =
                razorpayClient.orders.create(request);

        String razorpayOrderId = razorpayOrder.get("id").toString();

        paymentRepo.save(
                Payment.builder()
                        .orderId(appOrder.getId())
                        .razorpayOrderId(razorpayOrderId)
                        .amount(appOrder.getTotal())
                        .status(PaymentStatus.CREATED)   // ✅ FIX
                        .createdAt(Instant.now())
                        .build()
        );

        return new CreateRazorpayOrderResponse(
                razorpayOrderId,
                appOrder.getTotal() * 100,
                "INR",
                keyId
        );
    }

    @Transactional
    public void verifyPayment(RazorpayVerifyRequest req) throws Exception {

        Payment payment = paymentRepo.findByRazorpayOrderId(
                req.razorpayOrderId()
        ).orElseThrow(() -> new RuntimeException("Payment not found"));

        String payload =
                req.razorpayOrderId() + "|" + req.razorpayPaymentId();

        boolean isValid = Utils.verifySignature(
                payload,
                req.razorpaySignature(),
                razorpaySecret
        );

        if (!isValid) {
            throw new RuntimeException("Invalid Razorpay signature");
        }

        /* ================= UPDATE PAYMENT ================= */
        payment.setRazorpayPaymentId(req.razorpayPaymentId());
        payment.setRazorpaySignature(req.razorpaySignature());
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepo.save(payment);

        /* ================= UPDATE ORDER ================= */
        Order order = orderRepo.findById(payment.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.PAID);
        orderRepo.save(order);

        /* ================= 🔥 UNLOCK COURSES ================= */
        List<OrderItem> items =
                orderItemRepository.findByOrderId(order.getId());

        for (OrderItem item : items) {
            item.setStatus(PaymentStatus.SUCCESS);
            item.setPurchasedAt(LocalDateTime.now());
            orderItemRepository.save(item);
        }

        /* ================= GENERATE INVOICE ================= */
        invoiceService.generateInvoice(order.getId());
    }

}

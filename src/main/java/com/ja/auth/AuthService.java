package com.ja.auth;

import com.ja.admin.events.AdminEventPublisher;
import com.ja.auth.dto.*;
import com.ja.notification.EmailService;
import com.ja.notification.SmsService;
import com.ja.security.JwtUtil;
import com.ja.user.entity.User;
import com.ja.user.enums.AuthProvider;
import com.ja.user.enums.Role;
import com.ja.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;
    private final EmailService emailService;
    private final SmsService smsService;

    // ✅ ADD THIS
    private final AdminEventPublisher adminEventPublisher;

    /* =========================
       REGISTER (LOCAL USER)
    ========================== */
    public void register(RegisterRequest req) {

        if (repo.existsByEmail(req.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Email already registered"
            );
        }

        if (repo.existsByMobile(req.getMobile())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Mobile already registered"
            );
        }

        User u = new User();
        u.setName(req.getName());
        u.setEmail(req.getEmail());
        u.setMobile(req.getMobile());
        u.setPassword(encoder.encode(req.getPassword()));
        u.setRole(Role.USER);
        u.setProvider(AuthProvider.MOBILE);
        u.setEnabled(false);

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        u.setOtp(otp);
        u.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

        repo.save(u);

        try {
            emailService.sendOtp(req.getEmail(), otp);
        } catch (Exception e) {
            log.error("Email OTP failed: {}", e.getMessage());
        }

        try {
            smsService.sendOtp(req.getMobile(), otp);
        } catch (Exception e) {
            log.error("SMS OTP failed: {}", e.getMessage());
        }

        // 🔔 ADMIN EVENT
        adminEventPublisher.publish("""
        {
          "type": "USER_REGISTERED",
          "message": "New user registered"
        }
        """);

        log.info("User registered successfully");
    }

    /* =========================
       VERIFY OTP
    ========================== */
    public AuthResponse verifyOtp(OtpVerifyRequest req) {

        User user = repo.findByEmailAndDeletedFalse(req.getEmail())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "User not found"
                        )
                );

        if (!user.getOtp().equals(req.getOtp())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid OTP"
            );
        }

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "OTP expired"
            );
        }

        user.setEnabled(true);
        user.setOtp(null);
        user.setOtpExpiry(null);
        repo.save(user);

        String token = jwt.generateToken(user.getId(), user.getRole());
        return new AuthResponse(token);
    }

    /* =========================
       LOGIN
    ========================== */
    public AuthResponse login(LoginRequest req) {

        User user = repo.findByEmailAndDeletedFalse(req.getEmail())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED, "Invalid credentials"
                        )
                );

        if (user.getPassword() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "This account was created using Google/GitHub. Please login using OAuth."
            );
        }

        if (!user.isEnabled()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Please verify your email first"
            );
        }

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid credentials"
            );
        }

        String token = jwt.generateToken(user.getId(), user.getRole());
        return new AuthResponse(token);
    }

    /* =========================
       SET PASSWORD
    ========================== */
    public void setPassword(SetPasswordRequest req) {

        User user = repo.findByEmailAndDeletedFalse(req.getEmail())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "User not found"
                        )
                );

        if (user.getPassword() != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Password already set"
            );
        }

        user.setPassword(encoder.encode(req.getPassword()));
        user.setProvider(AuthProvider.MOBILE);
        repo.save(user);

        log.info("Password set for OAuth user: {}", user.getEmail());
    }
}

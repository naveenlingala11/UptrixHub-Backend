package com.ja.oauth;

import com.ja.security.JwtUtil;
import com.ja.user.entity.User;
import com.ja.user.enums.AuthProvider;
import com.ja.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // ✅ Google & GitHub avatar keys
        String avatar =
                oAuth2User.getAttribute("picture") != null
                        ? oAuth2User.getAttribute("picture")        // Google
                        : oAuth2User.getAttribute("avatar_url");    // GitHub

        User user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail(email);
                    u.setName(name);
                    u.setProvider(AuthProvider.GOOGLE); // or GITHUB
                    u.setEnabled(true);
                    return u;
                });

        // ✅ SAVE AVATAR ONLY IF NOT ALREADY SET
        if (user.getAvatar() == null && avatar != null) {
            user.setAvatar(avatar);
        }

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getId(), user.getRole());

        response.sendRedirect(
                frontendUrl + "/oauth-success?token=" + token
        );
    }
}
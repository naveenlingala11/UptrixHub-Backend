package com.ja.security;

import com.ja.oauth.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 🔥 CRITICAL FIX (ADDED – NO REDIRECT TO /login)
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                .authorizeHttpRequests(auth -> auth

                        // ✅ VERY IMPORTANT FOR CORS PREFLIGHT
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // WS / ACTUATOR
                        .requestMatchers("/ws/**", "/actuator/**").permitAll()

                        // AUTH + AI
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/admin/courses/**",
                                "/api/ai/**"
                        ).permitAll()

                        // ADMIN
                        .requestMatchers(
                                "/api/admin/**",
                                "/api/admin/bug-hunter/**"
                        ).hasRole("ADMIN")

                        // COURSES
                        .requestMatchers("/api/courses/**").permitAll()

                        // PAYMENT
                        .requestMatchers("/api/payment/**").permitAll()

                        // PSEUDO CODE
                        .requestMatchers("/api/pseudo-code/**").permitAll()

                        // PUBLIC
                        .requestMatchers("/api/public/**").permitAll()

                        // HOME + RESUME (AUTH REQUIRED)
                        .requestMatchers(
                                "/api/home/**",
                                "/api/resume/**",
                                "/api/resume-upload/**",
                                "/api/resume-match/**"
                        ).authenticated()

                        // BUG HUNTER + LEADERBOARD
                        .requestMatchers(
                                "/api/bug-hunter/**",
                                "/api/leaderboard/**"
                        ).authenticated()

                        // INVOICES
                        .requestMatchers("/api/invoices/**").authenticated()

                        // USER
                        .requestMatchers("/api/user/**").authenticated()

                        // COMPILER / EXECUTION (PUBLIC)
                        .requestMatchers(
                                "/api/compile/**",
                                "/api/execute/**",
                                "/api/**",
                                "/api/debug/**",
                                "/debug/**"
                        ).permitAll()

                        // OAUTH
                        .requestMatchers(
                                "/oauth2/**",
                                "/login/oauth2/**"
                        ).permitAll()

                        // CODE
                        .requestMatchers("/api/code/**").authenticated()

                        // SWAGGER
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // 🔒 EVERYTHING ELSE
                        .anyRequest().authenticated()
                )

                // ✅ OAUTH SUCCESS HANDLER
                .oauth2Login(oauth ->
                        oauth.successHandler(oAuth2LoginSuccessHandler)
                )

                // ✅ JWT FILTER
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}

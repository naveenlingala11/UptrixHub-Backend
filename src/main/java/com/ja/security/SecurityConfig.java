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

                // ✅ USE OUR CORS CONFIG
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        // ✅ VERY IMPORTANT FOR CORS PREFLIGHT (ADDED)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ================= EXISTING MATCHERS (UNCHANGED) =================
                        .requestMatchers("/ws/**", "/actuator/**").permitAll()
                        .requestMatchers("/api/auth/**","/api/admin/courses/").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/courses/**").permitAll()
                        .requestMatchers("/api/payment/**").permitAll()
                        .requestMatchers("/api/pseudo-code/**").permitAll()
                        .requestMatchers("/api/invoices/**").authenticated()
                        .requestMatchers("/api/user/**","/api/public/**").authenticated()
                        .requestMatchers(
                                "/api/compile/**",
                                "/api/execute/**",
                                "/api/**",
                                "/api/debug/**",
                                "/debug/**"
                        ).permitAll()

                        // ✅ OAUTH
                        .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()

                        .requestMatchers("/api/code/**").authenticated()

                        // ✅ SWAGGER
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

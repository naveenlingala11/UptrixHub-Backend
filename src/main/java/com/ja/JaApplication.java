package com.ja;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.ja")
@EnableAsync
public class JaApplication {

	public static void main(String[] args) {
		SpringApplication.run(JaApplication.class, args);
	}
	@PostConstruct
	public void init() {
		System.out.println("🔥 RESEND CONFIG LOADED 🔥");
	}

}

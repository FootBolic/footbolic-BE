package com.footbolic.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class FootbolicApplication {

	@Value("${profile.active}")
	private String ACTIVE_PROFILE;

	@Value("${domain.client.prod}")
	private String PROD_CLIETN_DOMAIN;

	@Value("${domain.client.dev}")
	private String DEV_CLIENT_DOMAIN;

	public static void main(String[] args) {
		SpringApplication.run(FootbolicApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins(ACTIVE_PROFILE.equals("prod") ? PROD_CLIETN_DOMAIN : DEV_CLIENT_DOMAIN)
						.allowedMethods("GET", "POST", "PATCH", "DELETE")
						.allowCredentials(true);
			}
		};
	}

}

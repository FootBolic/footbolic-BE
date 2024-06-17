package com.footbolic.api.config;

import com.footbolic.api.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            HandlerMappingIntrospector introspector
    ) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers(new AntPathRequestMatcher("/sign/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/members/public/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/members", "POST")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/menus/public/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/files/public/images/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/files/public/attachments/**")).permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }
}
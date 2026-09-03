package com.gym.management.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        .requestMatchers("/", "/register", "/login", "/error", "/error/**", "/favicon.ico").permitAll()
                        .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")
                        .requestMatchers("/reception", "/reception/**", "/receptionist", "/receptionist/**")
                        .hasAnyRole("RECEPTIONIST", "ADMIN")
                        .requestMatchers("/dashboard", "/dashboard/**")
                        .hasAnyRole("ATHLETE", "RECEPTIONIST", "ADMIN")
                        .requestMatchers("/dashboard/**", "/subscriptions/**", "/lockers/**")
                        .hasAnyRole("ATHLETE", "RECEPTIONIST", "ADMIN")
                        .requestMatchers("/traffic-log/**").hasAnyRole("ATHLETE", "RECEPTIONIST", "ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("mobileNumber")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/error/403"))
                .build();
    }

}

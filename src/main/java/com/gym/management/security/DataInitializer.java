package com.gym.management.security;

import com.gym.management.entity.User;
import com.gym.management.entity.enums.Role;
import com.gym.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String adminMobile = "09000000000";


        if (!userRepository.existsByMobileNumber(adminMobile)) {
            User admin = new User();
            admin.setFullName("Super Admin");
            admin.setMobileNumber(adminMobile);
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setIsActive(true);

            userRepository.save(admin);
                System.out.println(">>> Default Admin Created: Mobile=09000000000 | Password=admin123");
        }
    }
}
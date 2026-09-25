package org.example.ecommerce.config;

import org.example.ecommerce.entity.Role;
import org.example.ecommerce.entity.User;
import org.example.ecommerce.Repositories.UserRepository;
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
        if (!userRepository.existsByEmail("admin@ecommerce.com")) {
            User admin = User.builder()
                    .firstName("Admin")
                    .lastName("System")
                    .email("admin@ecommerce.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
            System.out.println(">>> Compte admin créé : admin@ecommerce.com / admin123");
        }
    }
}
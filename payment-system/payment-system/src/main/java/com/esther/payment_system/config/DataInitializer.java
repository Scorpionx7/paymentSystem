package com.esther.payment_system.config;

import com.esther.payment_system.entity.enums.Role;
import com.esther.payment_system.entity.User;
import com.esther.payment_system.repository.UserRepository;
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
    public void run(String... args) throws Exception {
        // Cria um usuário ADMIN se ele não existir
        if (userRepository.findByUsername("admin@payments.com").isEmpty()) {
            User adminUser = User.builder()
                    .username("admin@payments.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    // Este usuário não precisa de um 'Customer' associado
                    .build();
            userRepository.save(adminUser);
            System.out.println("Usuário ADMIN criado com sucesso!");
        }
    }


}

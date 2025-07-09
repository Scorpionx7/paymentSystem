package com.esther.payment_system.service.auth;


import com.esther.payment_system.dto.AuthenticationRequest;
import com.esther.payment_system.dto.AuthenticationResponse;
import com.esther.payment_system.dto.RegistrationRequest;
import com.esther.payment_system.entity.Account;
import com.esther.payment_system.entity.Customer;
import com.esther.payment_system.entity.User;
import com.esther.payment_system.entity.enums.Role;
import com.esther.payment_system.repository.AccountRepository;
import com.esther.payment_system.repository.CustomerRepository;
import com.esther.payment_system.repository.UserRepository;
import com.esther.payment_system.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    @Transactional
    public void register(RegistrationRequest request) {
        // 1. Verifica se o usuário já existe
        if (userRepository.findByUsername(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Usuário com este e-mail já existe.");
        }

        // 2. Cria o novo usuário
        User user = User.builder()
                .username(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);

        // 3. Cria o cliente associado
        Customer customer = Customer.builder()
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .user(user)
                .build();
        customerRepository.save(customer);

        // 4. Cria a conta para o cliente com saldo inicial zero
        Account account = Account.builder()
                .customer(customer)
                .balance(BigDecimal.ZERO)
                .build();
        accountRepository.save(account);
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

}

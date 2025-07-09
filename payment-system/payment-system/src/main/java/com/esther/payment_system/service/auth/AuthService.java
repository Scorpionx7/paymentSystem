package com.esther.payment_system.service.auth;

import com.esther.payment_system.dto.AuthenticationRequest;
import com.esther.payment_system.dto.AuthenticationResponse;
import com.esther.payment_system.dto.RegistrationRequest;

public interface AuthService {

    void register(RegistrationRequest request);
    AuthenticationResponse login(AuthenticationRequest request);
}

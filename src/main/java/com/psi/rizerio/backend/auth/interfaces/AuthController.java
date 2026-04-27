package com.psi.rizerio.backend.auth.interfaces;

import com.psi.rizerio.backend.auth.application.AuthService;
import com.psi.rizerio.backend.auth.application.dto.AuthRequest;
import com.psi.rizerio.backend.auth.application.dto.AuthResponse;
import com.psi.rizerio.backend.auth.application.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.psi.rizerio.backend.auth.application.dto.RegisterPatientRequest;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/register-patient")
    public ResponseEntity<AuthResponse> registerPatient(
            @RequestBody RegisterPatientRequest request
    ) {
        return ResponseEntity.ok(authService.registerPatient(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody AuthRequest request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }
}

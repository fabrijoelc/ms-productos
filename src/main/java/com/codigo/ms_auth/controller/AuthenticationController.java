package com.codigo.ms_auth.controller;

import com.codigo.ms_auth.aggregates.request.SignInRequest;
import com.codigo.ms_auth.aggregates.request.SignUpRequest;
import com.codigo.ms_auth.aggregates.response.SignInResponse;
import com.codigo.ms_auth.aggregates.response.SignUpResponse;
import com.codigo.ms_auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authentication/v1")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<SignUpResponse> register(@RequestBody SignUpRequest signUpRequest) {
        SignUpResponse response = authenticationService.signUp(signUpRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<SignInResponse> login(@RequestBody SignInRequest signInRequest) {
        SignInResponse response = authenticationService.signIn(signInRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        return authenticationService.validate(authHeader);
    }
}

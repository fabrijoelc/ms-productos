package com.codigo.ms_auth.service;

import com.codigo.ms_auth.aggregates.request.SignInRequest;
import com.codigo.ms_auth.aggregates.request.SignUpRequest;
import com.codigo.ms_auth.aggregates.response.SignInResponse;
import com.codigo.ms_auth.aggregates.response.SignUpResponse;
import com.codigo.ms_auth.entity.Usuario;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AuthenticationService {

    SignUpResponse signUp(SignUpRequest signUpRequest);

    SignInResponse signIn(SignInRequest signInRequest);

    boolean validateToken(String token);

    SignInResponse getTokenByRefreshToken(String token) throws IllegalAccessException;

    ResponseEntity<?> validate(String authHeader);

    List<Usuario> todos(); //
}


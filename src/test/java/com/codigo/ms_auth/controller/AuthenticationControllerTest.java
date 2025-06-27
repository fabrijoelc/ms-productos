package com.codigo.ms_auth.controller;

import com.codigo.ms_auth.aggregates.request.SignInRequest;
import com.codigo.ms_auth.aggregates.request.SignUpRequest;
import com.codigo.ms_auth.aggregates.response.SignInResponse;
import com.codigo.ms_auth.aggregates.response.SignUpResponse;
import com.codigo.ms_auth.service.AuthenticationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() {
        // Arrange
        SignUpRequest request = new SignUpRequest(
                "nombre", "apellido", "correo@email.com", "123456",
                "DNI", "12345678", "USER"
        );

        SignUpResponse expectedResponse = new SignUpResponse("Registro exitoso", "correo@email.com");

        when(authenticationService.signUp(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<SignUpResponse> response = authenticationController.register(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Registro exitoso", response.getBody().getMensaje());
        assertEquals("correo@email.com", response.getBody().getEmail());
    }

    @Test
    public void testLogin() {
        // Arrange
        SignInRequest request = new SignInRequest("correo@email.com", "123456");

        SignInResponse expectedResponse = new SignInResponse("mocked-jwt-token", "correo@email.com", List.of("USER"));

        when(authenticationService.signIn(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<SignInResponse> response = authenticationController.login(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("mocked-jwt-token", response.getBody().getToken());
        assertEquals("correo@email.com", response.getBody().getEmail());
        assertEquals(List.of("USER"), response.getBody().getRoles());
    }

    @Test
    public void testValidateToken() {
        // Arrange
        String token = "Bearer mocked-jwt-token";
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Token válido");

        when(authenticationService.validate(token)).thenReturn((ResponseEntity) expectedResponse);

        // Act
        ResponseEntity<?> response = authenticationController.validateToken(token);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Token válido", response.getBody());
    }
}


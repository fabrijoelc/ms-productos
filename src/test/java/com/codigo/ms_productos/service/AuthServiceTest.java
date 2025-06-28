package com.codigo.ms_productos.service;

import com.codigo.ms_productos.aggregates.request.SignInRequest;
import com.codigo.ms_productos.aggregates.response.SignInResponse;
import com.codigo.ms_productos.entity.Producto;
import com.codigo.ms_productos.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignIn() {
        // Arrange
        String email = "correo@prueba.com";
        String password = "clave123";

        SignInRequest request = new SignInRequest(email, password);

        Producto productoMock = new Producto();

        productoMock.setEmail(email);     // CORREGIDO
        productoMock.setPassword(password); // CORREGIDO

        Authentication authenticationMock = mock(Authentication.class);

        when(productoRepository.findByEmail(email)).thenReturn(Optional.of(productoMock));


        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authenticationMock);

        when(jwtService.generateToken(productoMock))
                .thenReturn("fake-jwt-token");

        // Act
        SignInResponse response = authService.signIn(request);

        // Assert
        assert response != null;
        assert "fake-jwt-token".equals(response.getToken());
    }
}

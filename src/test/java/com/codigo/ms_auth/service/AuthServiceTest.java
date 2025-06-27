package com.codigo.ms_auth.service;

import com.codigo.ms_auth.aggregates.request.SignInRequest;
import com.codigo.ms_auth.aggregates.response.SignInResponse;
import com.codigo.ms_auth.entity.Usuario;
import com.codigo.ms_auth.repository.UsuarioRepository;
import com.codigo.ms_auth.service.impl.AuthenticationServiceImpl;
import com.codigo.ms_auth.service.JwtService;
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
    private UsuarioRepository usuarioRepository;

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

        Usuario usuarioMock = new Usuario();

        usuarioMock.setEmail(email);     // CORREGIDO
        usuarioMock.setPassword(password); // CORREGIDO

        Authentication authenticationMock = mock(Authentication.class);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuarioMock));


        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authenticationMock);

        when(jwtService.generateToken(usuarioMock))
                .thenReturn("fake-jwt-token");

        // Act
        SignInResponse response = authService.signIn(request);

        // Assert
        assert response != null;
        assert "fake-jwt-token".equals(response.getToken());
    }
}

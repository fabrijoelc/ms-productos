package com.codigo.ms_productos.service.impl;

import com.codigo.ms_productos.aggregates.request.SignInRequest;
import com.codigo.ms_productos.aggregates.response.SignInResponse;
import com.codigo.ms_productos.entity.Producto;
import com.codigo.ms_productos.repository.ProductoRepository;
import com.codigo.ms_productos.service.ProductoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceImplTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private ProductoService productoService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignIn_ReturnsTokenWhenCredentialsValid() {
        // Arrange
        SignInRequest request = new SignInRequest("usuario", "123456");

        Producto producto = mock(Producto.class);
        when(productoRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(producto));
        when(producto.getRoles()).thenReturn(mockRoles());

        // Mock para UserDetailsService
        UserDetailsService userDetailsServiceMock = mock(UserDetailsService.class);
        when(productoService.userDetailsService()).thenReturn(userDetailsServiceMock);

        // Autenticación simulada correctamente
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));

        // Token generado
        when(jwtService.generateToken(producto)).thenReturn("mocked-jwt-token");

        // Act
        SignInResponse response = authenticationService.signIn(request);

        // Assert
        assertEquals("mocked-jwt-token", response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }


    @Test
    void testValidateToken_ValidTokenReturnsTrue() {
        String token = "valid.token.here";

        // 👇 Simular que se obtiene un UserDetails válido
        when(productoService.userDetailsService()).thenReturn(userDetailsService);
        when(jwtService.extractUserName(token)).thenReturn("usuario@email.com");
        when(userDetailsService.loadUserByUsername("usuario@email.com")).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(true);

        // Act
        boolean result = authenticationService.validateToken(token);

        // Assert
        assertTrue(result);
    }

    @Test
    void testValidateToken_InvalidTokenReturnsFalse() {
        String token = "invalid.token.here";

        // 👇 Simular token inválido
        when(productoService.userDetailsService()).thenReturn(userDetailsService);
        when(jwtService.extractUserName(token)).thenReturn("usuario@email.com");
        when(userDetailsService.loadUserByUsername("usuario@email.com")).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(false);

        // Act
        boolean result = authenticationService.validateToken(token);

        // Assert
        assertFalse(result);
    }

    private Set<Rol> mockRoles() {
        Rol rol = new Rol();
        rol.setNombre("USER");

        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        return roles;
    }
}

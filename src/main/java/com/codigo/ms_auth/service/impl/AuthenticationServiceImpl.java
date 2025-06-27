package com.codigo.ms_auth.service.impl;

import com.codigo.ms_auth.aggregates.constants.Constants;
import com.codigo.ms_auth.aggregates.request.SignInRequest;
import com.codigo.ms_auth.aggregates.request.SignUpRequest;
import com.codigo.ms_auth.aggregates.response.SignInResponse;
import com.codigo.ms_auth.aggregates.response.SignUpResponse;
import com.codigo.ms_auth.entity.Rol;
import com.codigo.ms_auth.entity.Usuario;
import com.codigo.ms_auth.repository.RolRepository;
import com.codigo.ms_auth.repository.UsuarioRepository;
import com.codigo.ms_auth.service.AuthenticationService;
import com.codigo.ms_auth.service.JwtService;
import com.codigo.ms_auth.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        Usuario usuario = getUsuarioEntity(signUpRequest);
        Rol rol = getRoles(signUpRequest.getRol());
        usuario.setRoles(Collections.singleton(rol));
        usuarioRepository.save(usuario);

        return new SignUpResponse("Usuario registrado correctamente", usuario.getEmail());
    }

    @Override
    public List<Usuario> todos() {
        return usuarioRepository.findAll();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            String username = jwtService.extractUserName(token);
            UserDetails userDetails = usuarioService.userDetailsService()
                    .loadUserByUsername(username);

            boolean result = jwtService.validateToken(token, userDetails)
                    && !jwtService.validateIsRefreshToken(token);

            log.info("Resultado de validación del token: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error al validar token: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getEmail(),
                        signInRequest.getPassword()
                )
        );

        Usuario usuario = usuarioRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String accessToken = jwtService.generateToken(usuario);

        List<String> roles = usuario.getRoles().stream()
                .map(Rol::getNombre)
                .toList();

        return SignInResponse.builder()
                .token(accessToken)
                .email(usuario.getEmail())
                .roles(roles)
                .build();
    }

    @Override
    public SignInResponse getTokenByRefreshToken(String token) throws IllegalAccessException {
        if (!jwtService.validateIsRefreshToken(token)) {
            throw new RuntimeException("Token ingresado no es un RefreshToken");
        }

        String userEmail = jwtService.extractUserName(token);

        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserDetails userDetails = usuarioService.userDetailsService()
                .loadUserByUsername(usuario.getUsername());

        if (!jwtService.validateToken(token, userDetails)) {
            throw new IllegalAccessException("El refresh token no es válido o está vencido");
        }

        String newAccessToken = jwtService.generateToken(usuario);

        List<String> roles = usuario.getRoles().stream()
                .map(Rol::getNombre)
                .toList();

        return SignInResponse.builder()
                .token(newAccessToken)
                .email(usuario.getEmail())
                .roles(roles)
                .build();
    }

    // 🔥 MÉTODO NUEVO: validate con ResponseEntity
    @Override
    public ResponseEntity<?> validate(String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String email = jwtService.extractUserName(token);

            UserDetails userDetails = usuarioService.userDetailsService().loadUserByUsername(email);
            if (!jwtService.validateToken(token, userDetails)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
            }

            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            Map<String, Object> response = new HashMap<>();
            response.put("email", usuario.getEmail());
            response.put("roles", usuario.getRoles().stream().map(Rol::getNombre).toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error en validate: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al validar token");
        }
    }

    // 🧱 Métodos auxiliares
    private Usuario getUsuarioEntity(SignUpRequest signUpRequest) {
        return Usuario.builder()
                .nombres(signUpRequest.getNombres())
                .apellidos(signUpRequest.getApellido())
                .email(signUpRequest.getEmail())
                .password(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()))
                .tipoDoc(signUpRequest.getTipoDoc())
                .numDoc(signUpRequest.getNumDoc())
                .isAccountNonExpired(Constants.STATUS_ACTIVE)
                .isAccountNonLocked(Constants.STATUS_ACTIVE)
                .isCredentialsNonExpired(Constants.STATUS_ACTIVE)
                .isEnabled(Constants.STATUS_ACTIVE)
                .build();
    }

    private Rol getRoles(String rolNombre) {
        return rolRepository.findByNombre(rolNombre)
                .orElseThrow(() -> new RuntimeException("Error: el rol no existe - " + rolNombre));
    }
}



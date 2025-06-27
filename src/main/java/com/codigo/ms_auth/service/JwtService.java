package com.codigo.ms_auth.service;


import com.codigo.ms_auth.entity.Usuario;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {

    String extractUserName(String token);
    String generateToken(Usuario usuario);
    boolean validateToken(String token, UserDetails userDetails);
    String generateRefreshToken(Map<String, Object> extraClaims,
                                UserDetails userDetails);
    boolean validateIsRefreshToken(String token);
    boolean isTokenValid(String token);

}

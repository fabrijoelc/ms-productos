package com.codigo.ms_productos.service.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class AuthValidator {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // URL del ms-auth para validar tokens
    private static final String URL_VALIDATE = "http://localhost:8080/auth/validate";

    // Roles permitidos para acceder
    private static final List<String> ROLES_PERMITIDOS = Arrays.asList("ADMIN", "SUPERADMIN");

    public AuthValidator() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public boolean tieneAcceso(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token.replace("Bearer ", ""));
            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    URL_VALIDATE,
                    HttpMethod.GET,
                    request,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode json = objectMapper.readTree(response.getBody());
                String rol = json.get("rol").asText();
                return ROLES_PERMITIDOS.contains(rol);
            }

        } catch (Exception e) {
            System.out.println("Error al validar token: " + e.getMessage());
        }

        return false;
    }
}

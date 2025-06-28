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

    // URL del endpoint de validación en ms-auth (AJUSTADO CORRECTAMENTE)
    private static final String URL_VALIDATE = "http://localhost:8080/apis/auth/api/authentication/v1/validate";

    // Roles permitidos para acceder a ms-productos
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

            System.out.println("Respuesta completa de validación: " + response.getBody());

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode json = objectMapper.readTree(response.getBody());
                JsonNode rolesNode = json.get("roles");

                if (rolesNode != null && rolesNode.isArray()) {
                    for (JsonNode rol : rolesNode) {
                        String rolTexto = rol.asText();
                        System.out.println("Rol detectado: " + rolTexto);
                        System.out.println("Comparando con permitidos: " + ROLES_PERMITIDOS);
                        if (ROLES_PERMITIDOS.contains(rolTexto)) {
                            System.out.println("✅ Acceso concedido");
                            return true;
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("❌ Error al validar token: " + e.getMessage());
        }

        System.out.println("🚫 Acceso denegado");
        return false;
    }
}


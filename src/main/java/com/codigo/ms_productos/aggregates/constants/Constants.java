package com.codigo.ms_productos.aggregates.constants;

public class Constants {

    // Rutas permitidas si deseas abrir alguna (actualmente no usadas)
    public static final String[] PERMIT_ENDPOINTS = {
            "/actuator/**"
    };

    // Endpoint para validar token con ms-auth
    public static final String URL_VALIDATE_TOKEN = "http://localhost:8080/auth/validate";

    // Roles válidos para acceder a productos
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_SUPERADMIN = "SUPERADMIN";

}

package com.codigo.ms_auth.aggregates.request;

import com.codigo.ms_auth.entity.Rol;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String nombres;
    private String apellidos;
    private String email;
    private String password;
    private String tipoDoc;
    private String numDoc;
    private String rol; // Debe ser string como "ADMIN", "USER", etc.
}

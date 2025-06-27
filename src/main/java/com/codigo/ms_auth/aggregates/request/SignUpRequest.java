package com.codigo.ms_auth.aggregates.request;

import com.codigo.ms_auth.entity.Rol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    private String nombres;
    private String apellido;
    private String email;
    private String password;
    private String tipoDoc;
    private String numDoc;
    private String rol; // Debe ser string como "ADMIN", "USER", etc.
}

package com.codigo.ms_auth.aggregates.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignUpResponse {
    private String mensaje;
    private String email;
}

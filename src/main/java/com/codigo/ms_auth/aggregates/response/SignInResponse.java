package com.codigo.ms_auth.aggregates.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SignInResponse {
    private String token;
    private String email;
    private List<String> roles;
}

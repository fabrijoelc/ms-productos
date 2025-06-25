package com.codigo.ms_auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/v1")
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<String> getUserProfile() {
        return ResponseEntity.ok("Hola USER, este es tu perfil privado");
    }
}


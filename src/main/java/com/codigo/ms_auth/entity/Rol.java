package com.codigo.ms_auth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rol implements GrantedAuthority {

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
    public static final String SUPERADMIN = "SUPERADMIN";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Override
    public String getAuthority() {
        return nombre;
    }
}


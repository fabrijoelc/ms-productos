package com.codigo.ms_auth.repository;

import com.codigo.ms_auth.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByRoles_Nombre(String nombre);
    Optional<Usuario> findByEmailAndPassword(String email, String password);
}

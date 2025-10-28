package com.miaplicacion.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miaplicacion.demo.model.Estudiante;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByEmail(String email);
    boolean existsByEmail(String email);
}

package com.miaplicacion.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miaplicacion.demo.model.Estudiante;
import com.miaplicacion.demo.repository.EstudianteRepository;



@RestController
@RequestMapping("/estudiantes")
public class EstudianteController {
    private final EstudianteRepository repository;

    public EstudianteController(EstudianteRepository repository) {
        this.repository = repository;
    }
    @GetMapping
    public List<Estudiante> obtenerTodos() {
        return repository.findAll();
    }
    @PostMapping
    public Estudiante crearEstudiante(@RequestBody Estudiante estudiante) {
        return repository.save(estudiante);
    }

}
package com.miaplicacion.demo.service;

import com.miaplicacion.demo.repository.EstudianteRepository;
import com.miaplicacion.demo.model.Estudiante;
import com.miaplicacion.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class EstudianteService {

    private final EstudianteRepository repo;

    public EstudianteService(EstudianteRepository repo) {
        this.repo = repo;
    }

    public List<Estudiante> findAll() {
        return repo.findAll();
    }

    public Estudiante findById(Long id) {
        return repo.findById(id)
                   .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id " + id));
    }

    public Estudiante save(Estudiante e) {
        return repo.save(e);
    }

    public Estudiante update(Long id, Estudiante datos) {
        Estudiante e = findById(id);
        e.setNombre(datos.getNombre());
        e.setEmail(datos.getEmail());
        e.setEdad(datos.getEdad());
        return repo.save(e);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Estudiante no encontrado con id " + id);
        }
        repo.deleteById(id);
    }
}

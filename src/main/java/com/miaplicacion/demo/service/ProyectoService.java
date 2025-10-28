package com.miaplicacion.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miaplicacion.demo.model.Proyecto;
import com.miaplicacion.demo.repository.ProyectoRepository;

@Service
public class ProyectoService {

    @Autowired
    private ProyectoRepository proyectoRepository;

    public List<Proyecto> findAll() {
        return proyectoRepository.findAll();
    }

    public Optional<Proyecto> findById(String id) {
        return proyectoRepository.findById(id);
    }

    public Proyecto save(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    public void deleteById(String id) {
        proyectoRepository.deleteById(id);
    }
}
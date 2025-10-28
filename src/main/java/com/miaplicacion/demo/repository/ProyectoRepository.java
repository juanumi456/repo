package com.miaplicacion.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.miaplicacion.demo.model.Proyecto;

@Repository
public interface ProyectoRepository extends MongoRepository<Proyecto, String> {
}
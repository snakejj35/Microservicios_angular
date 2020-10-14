package com.formacionbdi.microservicios.app.examenes.models.repository;

import org.springframework.data.repository.CrudRepository;

import com.formacionbdi.microservicios.common.examenes.models.entity.Asignatura;

public interface IAsignaturaRepository extends CrudRepository<Asignatura, Long> {
	
}

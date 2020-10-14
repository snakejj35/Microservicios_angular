package com.formacionbdi.microservicios.app.usuarios.service;

import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;

import java.util.List;

import com.formacionbdi.microservicios.common.services.ICommonService;

public interface IAlumnoService extends ICommonService<Alumno>{
	
	public List<Alumno> findByNombreApellido(String value);
	
	public Iterable<Alumno> findAllById(Iterable<Long> ids);
	
	public void eliminarCursoAlumnoPorId(Long id);
}

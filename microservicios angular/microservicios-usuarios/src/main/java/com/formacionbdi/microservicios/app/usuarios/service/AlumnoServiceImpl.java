package com.formacionbdi.microservicios.app.usuarios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacionbdi.microservicios.app.usuarios.client.ICursoFeignClient;
import com.formacionbdi.microservicios.app.usuarios.models.repository.IAlumnoRepository;
import com.formacionbdi.microservicios.common.services.CommonServiceImpl;
import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;

@Service
public class AlumnoServiceImpl extends CommonServiceImpl<Alumno, IAlumnoRepository> implements IAlumnoService {

	@Autowired 
	private ICursoFeignClient clientCurso;
	
	@Override
	@Transactional(readOnly = true)
	public List<Alumno> findByNombreApellido(String value) {
		return repository.findByNombreApellido(value);
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<Alumno> findAllById(Iterable<Long> ids) {
		return repository.findAllById(ids);
	}

	//para que se pueda implementar neceita ser ejecutado por el metodo transaccional deletById de la clase padrea
	@Override 
	public void eliminarCursoAlumnoPorId(Long id) { //es un cliente http por eso no debe llevar @Transactional
		clientCurso.eliminarCursoAlumnoPorId(id);
	}

	//Se ejecuta y elimina al alumno de postgres, depues elimina mediante API REST el CursoAlumno 
	@Override
	@Transactional
	public void deleteById(Long id) {
		super.deleteById(id);
		this.eliminarCursoAlumnoPorId(id);
	}

	@Override  
	@Transactional(readOnly = true)
	public Iterable<Alumno> findAll() {
		return repository.findAllByOrderByIdAsc();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Alumno> findAll(Pageable pageable) {
		return repository.findAllByOrderByIdAsc(pageable);
	}
	
}

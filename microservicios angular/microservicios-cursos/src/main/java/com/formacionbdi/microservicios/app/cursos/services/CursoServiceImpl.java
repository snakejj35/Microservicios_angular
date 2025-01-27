package com.formacionbdi.microservicios.app.cursos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacionbdi.microservicios.app.cursos.clients.IAlumnoFeignClient;
import com.formacionbdi.microservicios.app.cursos.clients.IRespuestaFeignClient;
import com.formacionbdi.microservicios.app.cursos.models.entity.Curso;
import com.formacionbdi.microservicios.app.cursos.models.repository.ICursoRepository;
import com.formacionbdi.microservicios.common.services.CommonServiceImpl;
import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;

@Service
public class CursoServiceImpl extends CommonServiceImpl<Curso, ICursoRepository> implements ICursoService {

	@Autowired
	private IRespuestaFeignClient clientRespuesta;
	
	@Autowired
	private IAlumnoFeignClient clientAlumno;
	
	@Override
	@Transactional(readOnly = true)
	public Curso findCursoByAlumnoId(Long id) {
		return repository.findCursoByAlumnoId(id);
	}

	@Override     //No es Transactional por que no es parte de un reppositorio de base de datos
	public Iterable<Long> obtenerExamenesIdsConRespuestasAlumno(Long idAlu) {
		return clientRespuesta.obtenerExamenesIdsConRespuestasAlumno(idAlu);
	}

	@Override
	public Iterable<Alumno> obtenerAlumnosPorCurso(Iterable<Long> ids) {
		return clientAlumno.obtenerAlumnosPorCurso(ids);
	}

	@Override
	@Transactional
	public void deleteCursoAlumnoById(Long id) {
		repository.deleteCursoAlumnoById(id);
	}

}

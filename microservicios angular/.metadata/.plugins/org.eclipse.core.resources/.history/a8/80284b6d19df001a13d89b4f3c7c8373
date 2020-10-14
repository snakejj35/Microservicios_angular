package com.formacionbdi.microservicios.app.cursos.services;

import com.formacionbdi.microservicios.app.cursos.models.entity.Curso;
import com.formacionbdi.microservicios.common.services.ICommonService;
import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;

public interface ICursoService extends ICommonService<Curso> {
	
	public Curso findCursoByAlumnoId(Long id);
	
	public Iterable<Long> obtenerExamenesIdsConRespuestasAlumno(Long idAlu);
	
	public Iterable<Alumno> obtenerAlumnosPorCurso(Iterable<Long> ids);
	
	public void deleteCursoAlumnoById(Long id);
}

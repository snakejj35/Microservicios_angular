package com.formacionbdi.microservicios.app.cursos.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;

//Consume el servicio o endpint de Rest de AlumnoController "obtenerAlumnosPorCurso"
@FeignClient(name = "microservicio-usuarios")
public interface IAlumnoFeignClient {
	
	//El end point debe tener la misma anotacion, ruta y el mismo nombre
	@GetMapping("/alumnos-por-curso")
	public Iterable<Alumno> obtenerAlumnosPorCurso(@RequestParam Iterable<Long> ids);

}

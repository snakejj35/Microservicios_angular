package com.formacionbdi.microservicios.app.usuarios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservicio-cursos")
public interface ICursoFeignClient {
	
	//Consumiendo el endpoint eliminar-alumno del microservicio-cursos mediante rest para eliminar el alumno de un curso
	@DeleteMapping("/eliminar-alumno/{id}")
	public void eliminarCursoAlumnoPorId(@PathVariable Long id);
}

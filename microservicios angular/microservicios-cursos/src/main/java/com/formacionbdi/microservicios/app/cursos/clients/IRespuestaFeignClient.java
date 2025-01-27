package com.formacionbdi.microservicios.app.cursos.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
//Consume el servicio y endpint de Rest de RespuestaController 
@FeignClient(name="microservicio-respuestas")
public interface IRespuestaFeignClient {   //Interfaz declarativa para comunicar con microservicio respuestas. Por medio del idntificador o nombre del microservicio
	                                       //Configurar un metodo con el endpoint al cual se va a comunicar
	@GetMapping("/alumno/{idAlu}/examenes-respondidos")
	public Iterable<Long> obtenerExamenesIdsConRespuestasAlumno(@PathVariable Long idAlu);
}

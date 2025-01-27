package com.formacionbdi.microservicios.app.respuestas.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.formacionbdi.microservicios.common.examenes.models.entity.Examen;

@FeignClient(name = "microservicio-examenes")
public interface IExamenFeignClient {
	
	@GetMapping("/{id}")
	public Examen obtenerExamenById(@PathVariable Long id); 
	
	@GetMapping("/examenes-respondidos")
	public List<Long> obtenerExamenIdsPorPreguntasIdsRespondidas(@RequestParam List<Long> preguntaIds);
}
	 
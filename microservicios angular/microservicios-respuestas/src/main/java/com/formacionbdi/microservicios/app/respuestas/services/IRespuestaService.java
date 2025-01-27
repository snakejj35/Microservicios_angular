package com.formacionbdi.microservicios.app.respuestas.services;

import java.util.List;

import com.formacionbdi.microservicios.app.respuestas.models.entity.Respuesta;
import com.formacionbdi.microservicios.common.examenes.models.entity.Examen;

public interface IRespuestaService {
	
	public Iterable<Respuesta> saveAll(Iterable<Respuesta> respuestas);
	
	public List<Respuesta> findRespuestaByAlumnoByExamen(Long alumnoId, Long examenId);
	
	public Iterable<Long> findExamenesIdsWithRespuestasByAlumno(Long alumniId);
	
	public Examen obtenerExamenById(Long id); //Metodo de IExamenFeignClient
	
	public Iterable<Respuesta> findByAlumnoId(Long alumnoId);
	
	//public List<Long> obtenerExamenIdsPorPreguntasIdsRespondidas(List<Long> preguntaIds); //Metodo de IExamenFeignClient
		
}

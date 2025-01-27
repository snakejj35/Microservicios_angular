package com.formacionbdi.microservicios.app.respuestas.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.microservicios.app.respuestas.models.entity.Respuesta;
import com.formacionbdi.microservicios.app.respuestas.services.IRespuestaService;

@RestController
public class RespuestaController{
	
	@Autowired
	private IRespuestaService service;
	
	//Se modifica el metodo para asignar el alumnoId a las Respuestas, usando API stream de java 8
	@PostMapping
	private ResponseEntity<?> crearRespuestas(@RequestBody Iterable<Respuesta> respuestas) {
		respuestas = ((List<Respuesta>) respuestas).stream().map(r -> {
			r.setAlumnoId(r.getAlumno().getId());
			r.setPreguntaId(r.getPregunta().getId()); // se agrega el Id: Cuando se envia el conjunto de respuestas, ya viene con el objeto alumno y el objeto pregunta
			return r;                                 //Por cada respuesta de la pregunta y del alumno
		}).collect(Collectors.toList());
		Iterable<Respuesta> respuestasDb = service.saveAll(respuestas);	
		return ResponseEntity.status(HttpStatus.CREATED).body(respuestasDb);
	}
	
	@GetMapping("/alumno/{idAlu}/examen/{idExa}")
	public ResponseEntity<?> obtenerRespuestaPorAlumnoPorExamen(@PathVariable Long idAlu, @PathVariable Long idExa) {
		Iterable<Respuesta> respuestas = service.findRespuestaByAlumnoByExamen(idAlu, idExa);
		return ResponseEntity.ok(respuestas);
	}
	
	@GetMapping("/alumno/{idAlu}/examenes-respondidos")
	public ResponseEntity<?> obtenerExamenesIdsConRespuestasAlumno(@PathVariable Long idAlu) {
		Iterable<Long> examenesIds = service.findExamenesIdsWithRespuestasByAlumno(idAlu);
		return ResponseEntity.ok(examenesIds);
	}
}

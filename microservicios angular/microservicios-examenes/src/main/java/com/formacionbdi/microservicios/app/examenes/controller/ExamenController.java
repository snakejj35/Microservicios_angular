package com.formacionbdi.microservicios.app.examenes.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.microservicios.app.examenes.services.IExamenService;
import com.formacionbdi.microservicios.common.controllers.CommonController;
import com.formacionbdi.microservicios.common.examenes.models.entity.Examen;
import com.formacionbdi.microservicios.common.examenes.models.entity.Pregunta;

@RestController
public class ExamenController extends CommonController<Examen, IExamenService>{
	
	@GetMapping("/examenes-respondidos")
	public ResponseEntity<?> obtenerExamenIdsPorPreguntasIdsRespondidas(@RequestParam List<Long> preguntaIds) {
		return ResponseEntity.ok().body(service.findExamenesIdsWithRespuestasByPreguntasIds(preguntaIds));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody Examen examen, BindingResult result, @PathVariable Long id) {
		if(result.hasErrors()) {
			return this.validar(result);
		}
		
		Optional<Examen> opt = service.finById(id);
		
		if(!opt.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Examen examenDb = opt.get();
		examenDb.setNombre(examen.getNombre());
		
	
		//Preguntas contenidas en el Json que Recorre la lista para mostrar las preguntas a eliminar
		List<Pregunta> eliminadas = examenDb.getPreguntas()
		.stream()
		.filter(pdb -> !examen.getPreguntas().contains(pdb))
		.collect(Collectors.toList());
		
		//se separa el stream para que no este en el mismo flujo y pueda eliminar
		eliminadas.forEach(examenDb :: removePregunta);

		//Por cada pregunta a eliminar se suprime
//		eliminadas.forEach(p -> {
//			examenDb.removePregunta(p);
//		});
		
		//preguntas a eliminar que no estan contenidas en el JSON

		//Agregar y modificar preguntas
//        examenDb.setPreguntas(examen.getPreguntas()); comente estas 3 lineas
//        examenDb.setAsignaturaHija(examen.getAsignaturaHija()); //agrega al set la hija para que se pueda actualizar
//        examenDb.setAsignaturaPadre(examen.getAsignaturaPadre()); //agrega al set el padre para que se pueda actualizar
        
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(examenDb));
	}
	
	@GetMapping("/filtrar/{term}")
	public ResponseEntity<?> filtrar(@PathVariable String term) {
		return ResponseEntity.ok(service.findByNombre(term));
	}
	
	@GetMapping("/asignaturas")
	public ResponseEntity<?> getAsignaturas() {
		return ResponseEntity.ok(service.findAllAsignaturas());
	}

}

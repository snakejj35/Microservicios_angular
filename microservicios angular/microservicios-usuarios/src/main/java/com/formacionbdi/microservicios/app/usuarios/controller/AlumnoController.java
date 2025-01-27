package com.formacionbdi.microservicios.app.usuarios.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.formacionbdi.microservicios.app.usuarios.service.IAlumnoService;
import com.formacionbdi.microservicios.common.controllers.CommonController;
import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;

@RestController
public class AlumnoController extends CommonController<Alumno, IAlumnoService>{
	
	@GetMapping("/alumnos-por-curso")
	public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam List<Long> ids) {
		return ResponseEntity.ok(service.findAllById(ids));
	}
	
	@GetMapping("/uploads/img/{id}")
	public ResponseEntity<?> verImagen(@PathVariable Long id) {	
		Optional<Alumno> optionalAlumno = service.finById(id);
		
		if(!optionalAlumno.isPresent() || optionalAlumno.get().getFoto() == null) {
			return ResponseEntity.notFound().build();  
		}		
		
		Resource imagen = new ByteArrayResource(optionalAlumno.get().getFoto());
		
		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_JPEG)
				.body(imagen);
	}
	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody Alumno alumno, BindingResult result, @PathVariable Long id) {
		if(result.hasErrors()) {
			return this.validar(result);
		}
		
		Optional<Alumno> optionalAlumno = service.finById(id);
		if(!optionalAlumno.isPresent()) {
			return ResponseEntity.notFound().build();  //Construye la respuesta para un notfound 404
		}
		
		Alumno alumnoDB = optionalAlumno.get();
		alumnoDB.setNombre(alumno.getNombre());
		alumnoDB.setApellido(alumno.getApellido());
		alumnoDB.setEmail(alumno.getEmail());
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(alumnoDB));
	}
	
	@GetMapping("/filtrar/{term}")
	public ResponseEntity<?> filtrar(@PathVariable String term) {
		return ResponseEntity.ok(service.findByNombreApellido(term));
	}

	@PostMapping("/crear-img")
	public ResponseEntity<?> crearImagen(@Valid Alumno alumno, BindingResult result, 
			@RequestParam MultipartFile file) {

		if(!file.isEmpty()) {
			try {
				alumno.setFoto(file.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return super.crear(alumno, result);
	}
	
	@PutMapping("/editar-img/{id}")
	public ResponseEntity<?> editarImagen(@Valid Alumno alumno, BindingResult result, @PathVariable Long id,
			@RequestParam MultipartFile file) {
		if(result.hasErrors()) {
			return this.validar(result);
		}
		
		Optional<Alumno> optionalAlumno = service.finById(id);
		if(!optionalAlumno.isPresent()) {
			return ResponseEntity.notFound().build();  //Construye la respuesta para un notfound 404
		}
		
		Alumno alumnoDB = optionalAlumno.get();
		alumnoDB.setNombre(alumno.getNombre());
		alumnoDB.setApellido(alumno.getApellido());
		alumnoDB.setEmail(alumno.getEmail());
		
		if(!file.isEmpty()) {
			try {
				alumnoDB.setFoto(file.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(alumnoDB));
	}
	
}

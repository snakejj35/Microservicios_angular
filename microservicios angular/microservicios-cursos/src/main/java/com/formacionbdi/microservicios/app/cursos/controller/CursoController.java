package com.formacionbdi.microservicios.app.cursos.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.microservicios.app.cursos.models.entity.Curso;
import com.formacionbdi.microservicios.app.cursos.models.entity.CursoAlumno;
import com.formacionbdi.microservicios.app.cursos.services.ICursoService;
import com.formacionbdi.microservicios.common.controllers.CommonController;
import com.formacionbdi.microservicios.common.examenes.models.entity.Examen;
import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;

@RestController
public class CursoController extends CommonController<Curso, ICursoService> {

	@Value("${config.balanceador.test}")   //Se injecta esta variable de entorno que se utilizara como constante.
	private String balanceador_test;
	
	@DeleteMapping("/eliminar-alumno/{id}")
	public ResponseEntity<?> eliminarCursoAlumnoPorId(@PathVariable Long id) {
		service.deleteCursoAlumnoById(id);
		return ResponseEntity.noContent().build();
	}
	
	//Se van a listar los alumnos colo con el id por cada cursoAlumno de esta nueva relacion
	@GetMapping //usa la misma ruta de listado
	@Override //metodo sebreescrito de CommonController
	public ResponseEntity<?> listar() {  
		List<Curso> cursos = ((List<Curso>) service.findAll()).stream().map(c -> {   //Se hace el cast de Iterable y se encierra entre parantesis para obtener sus propiedades
			c.getCursoAlumnos().forEach(ca -> {
				Alumno alumno = new Alumno();
				alumno.setId(ca.getAlumnoId());
				c.addAlumno(alumno);
			});
			return c;
		}).collect(Collectors.toList());
		return ResponseEntity.ok().body(cursos);  
	}
	
	@GetMapping("/pagina")
	@Override
	public ResponseEntity<?> listar(Pageable pageable) {  //Listar paginable: Pagina Detalle que muestra la lista de los alumnos con un curso y ID cursos_alumnos
		Page<Curso> cursos = service.findAll(pageable).map(curso -> { //Contiene Toda la informacion de la paginacion: pagina actual, contenido, cantidad de elementos x pagina, rango.
			curso.getCursoAlumnos().forEach(ca -> {
				Alumno alumno = new Alumno();
				alumno.setId(ca.getAlumnoId());
				curso.addAlumno(alumno);
			});
			return curso;
		});
		return ResponseEntity.ok().body(cursos);  
	}
	
	//Se va a obtener los alumnos del curso aunque solo tiene los ids, pero mostrara sus datos completos
	@GetMapping("/{id}")   
	@Override //metodo sebreescrito de CommonController
	public ResponseEntity<?> ver(@PathVariable Long id) {
		Optional<Curso> opt = service.finById(id);
		if(!opt.isPresent()) {   
			return ResponseEntity.notFound().build();  
		}
		Curso curso = opt.get();
		if(curso.getCursoAlumnos().isEmpty() == false) {  //verifica que cursos tenga alumnos asignados
			List<Long> ids = curso.getCursoAlumnos().stream().map(ca -> {   //Se obtiene los ids de  los alumnos
				return ca.getAlumnoId();
			}).collect(Collectors.toList());
			
			List<Alumno> alumnos = (List<Alumno>) service.obtenerAlumnosPorCurso(ids);
			curso.setAlumnos(alumnos);
		}
		
		return ResponseEntity.ok().body(curso);	
	}
	
	//Metodo sobreescrito. Balncea la carga de los cursos
	@GetMapping("/balanceador-test")
	public ResponseEntity<?> balanceadorTest() {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("balanceador", balanceador_test);
		response.put("cursos", service.findAll());
		return ResponseEntity.ok(response);  //Se va a generar un json con atributos: balanceador y cursos
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> editar (@Valid @RequestBody Curso curso,BindingResult result, @PathVariable Long id){
		if(result.hasErrors()) {
			return this.validar(result);
		}
		
		Optional<Curso> opt = this.service.finById(id);
		
		if(!opt.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		Curso dbCurso = opt.get();
		dbCurso.setNombre(curso.getNombre());
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(dbCurso));
	}
	
	@PutMapping("/{id}/asignar-alumnos")
	public ResponseEntity<?> asignarAlumnos(@RequestBody List<Alumno> alumnos, @PathVariable Long id) {
		Optional<Curso> opt = this.service.finById(id);
		if(!opt.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso dbCurso = opt.get();
		
		alumnos.forEach(alu -> {		
		CursoAlumno cursoAlumno = new CursoAlumno();
		cursoAlumno.setAlumnoId(alu.getId());
		cursoAlumno.setCurso(dbCurso);
		
		dbCurso.addCursoAlumno(cursoAlumno);
		});
		
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(dbCurso));
	}
	
	@PutMapping("/{id}/eliminar-alumno")
	public ResponseEntity<?> eliminarAlumno(@RequestBody Alumno alumno, @PathVariable Long id) {
		Optional<Curso> opt = this.service.finById(id);
		if(!opt.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso dbCurso = opt.get();
		CursoAlumno cursoAlumno = new CursoAlumno();
		cursoAlumno.setAlumnoId(alumno.getId());
		dbCurso.removeCursoAlumno(cursoAlumno);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(dbCurso));
	}
	
	@GetMapping("/alumno/{id}")
	public ResponseEntity<?> buscarPorAlumnoId(@PathVariable Long id) {
		Curso curso = service.findCursoByAlumnoId(id);
		
		if(curso != null) {
			//Se realiza un request mediante API REST consumiendo el servicio respuestas una sola vez y retorna lista de examenesIds
			List<Long> examenesIds = (List<Long>) service.obtenerExamenesIdsConRespuestasAlumno(id);   //Lista de examenes respondidos
	        
			if(examenesIds != null && examenesIds.size() > 0) {
			//Se obtiene una nueva lista a partir de la original. crea un stream por de los examenes respondidos por el alumno
				List<Examen> examenes = curso.getExamenes().stream().map(examen -> {
					if(examenesIds.contains(examen.getId())) {
						examen.setRespondido(true);  //Bandera que indica que un examen fue respondido
					}
					return examen; //El map retorna el objeto modificado
				}).collect(Collectors.toList());  //Convierte la coleccion de tipo List
			
				curso.setExamenes(examenes);  //Status: Se cambian los examenes del curso a estatus modificado con la bandera examenes que valida que se hayan respondido los examenes
			}
		}	 
		return ResponseEntity.ok(curso);
	}
	
	@PutMapping("/{id}/asignar-examenes")
	public ResponseEntity<?> asignarExamenes(@RequestBody List<Examen> examenes, @PathVariable Long id) {
		Optional<Curso> opt = this.service.finById(id);
		if(!opt.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso dbCurso = opt.get();
		
		examenes.forEach(e -> {
		dbCurso.addExamen(e);
		});
		
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(dbCurso));
	}
	
	@PutMapping("/{id}/eliminar-examen")
	public ResponseEntity<?> eliminarExamen(@RequestBody Examen examen, @PathVariable Long id) {
		Optional<Curso> opt = this.service.finById(id);
		if(!opt.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso dbCurso = opt.get();
		
		dbCurso.removeExamen(examen);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(dbCurso));
	}
}

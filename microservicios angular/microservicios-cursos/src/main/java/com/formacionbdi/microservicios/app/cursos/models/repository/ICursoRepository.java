package com.formacionbdi.microservicios.app.cursos.models.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.formacionbdi.microservicios.app.cursos.models.entity.Curso;

public interface ICursoRepository extends PagingAndSortingRepository<Curso, Long>{
	
	@Query("SELECT c FROM Curso c JOIN FETCH c.cursoAlumnos a WHERE a.alumnoId=?1")
	public Curso findCursoByAlumnoId(Long id);
	
	@Modifying   //Esta anotacion se agrega cada ves que se utiliza un DELETE o UPDATE o INSERT a las tablas utlilizando la Anotacion @Query
	@Query("DELETE FROM CursoAlumno ca WHERE ca.alumnoId=?1")  //Cuendo se elimine el Alumno en "postgres.alumnos" en cascada se eliminara ID del alumno de "mysql.cursos" y de "mysql.cursos_alumnos" con la Clase CursoAlumno en base a su atributo "Long alumnoId" 
	public void deleteCursoAlumnoById(Long id);
}

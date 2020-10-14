package com.formacionbdi.microservicios.app.usuarios.models.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;

//Se cambia de CrudRepository a PagingAndSortingRepository para paginacion
public interface IAlumnoRepository extends PagingAndSortingRepository<Alumno, Long> {

	//La busqueda o filtro de alumnos por nombre o apellido es sencieble a mayuscular y minusculas en Postgres, por eso se modifica la consulta con UPPER
	//@Query("SELECT a from Alumno a where a.nombre like %?1% or a.apellido like %?1%")     //Select con mysql
	
	@Query("SELECT a from Alumno a where upper(a.nombre) like upper(concat('%', ?1, '%')) or upper(a.apellido) like upper(concat('%', ?1, '%'))")   //Select con Postgres convirtiendolo a mayusculas y concatenando el resultado para que lo soporte
	public List<Alumno> findByNombreApellido(String value);
	
	public Iterable<Alumno> findAllByOrderByIdAsc(); //Que ordene Ascendentemente anque se modifiquen los registros
	
	public Page<Alumno> findAllByOrderByIdAsc(Pageable pageable); //Que ordene tabien Ascendentemente pero Con paginacion (page y size)
}

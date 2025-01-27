package com.formacionbdi.microservicios.app.examenes.models.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.formacionbdi.microservicios.common.examenes.models.entity.Examen;

public interface IExamenRepository extends PagingAndSortingRepository<Examen, Long>{

	@Query("SELECT e FROM Examen e WHERE e.nombre like %?1%")
	public List<Examen> findByNombre(String value);
	
	/** Que devuelva una lista de todos los examenes respondidos*/
	@Query("SELECT e.id FROM Pregunta p JOIN p.examen e WHERE p.id in ?1 GROUP BY e.id")
	public Iterable<Long> findExamenesIdsWithRespuestasByPreguntasIds(Iterable<Long> preguntasIds);		
}

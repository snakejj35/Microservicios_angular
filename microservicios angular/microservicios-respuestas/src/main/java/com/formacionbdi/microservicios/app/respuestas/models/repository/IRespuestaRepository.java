package com.formacionbdi.microservicios.app.respuestas.models.repository;
 
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.formacionbdi.microservicios.app.respuestas.models.entity.Respuesta;

public interface IRespuestaRepository extends MongoRepository<Respuesta, String>{  //Cambia el respositorio CrudRepository a MongoRepository, la llave de Long a String
	
	@Query("{'alumnoId': ?0, 'preguntaId': { $in: ?1} }") //Consulta por alumno y por un conjunto de preguntas
	public Iterable<Respuesta> findRespuestaByAlumnoByPreguntaIds(Long alumnoId, Iterable<Long> preguntaIds);
	
	@Query("{'alumnoId': ?0}")
	public Iterable<Respuesta> findByAlumnoId(Long alumnoId);
	
	/** Fetch para que en la consulta cargue todos los objetos relaciondos a las respuestas*/
	//@Query("SELECT r FROM Respuesta r JOIN FETCH r.pregunta p JOIN FETCH p.examen e WHERE r.alumnoId=?1 AND e.id=?2")
	//public Iterable<Respuesta> findRespuestaByAlumnoByExamen(Long alumnoId, Long examenId);
	
	/** Que devuelva todos los examenes. Una lista de todos los examenes respondidos*/
	//@Query("SELECT e.id FROM Respuesta r JOIN r.pregunta p JOIN p.examen e WHERE r.alumnoId=?1 GROUP BY e.id")
	//public Iterable<Long> findExamenesIdsWithRespuestasByAlumno(Long alumnoId);
}

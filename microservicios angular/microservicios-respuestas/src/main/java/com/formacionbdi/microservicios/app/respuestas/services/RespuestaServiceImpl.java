package com.formacionbdi.microservicios.app.respuestas.services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formacionbdi.microservicios.app.respuestas.clients.IExamenFeignClient;
import com.formacionbdi.microservicios.app.respuestas.models.entity.Respuesta;
import com.formacionbdi.microservicios.app.respuestas.models.repository.IRespuestaRepository;
import com.formacionbdi.microservicios.common.examenes.models.entity.Examen;
import com.formacionbdi.microservicios.common.examenes.models.entity.Pregunta;

@Service
public class RespuestaServiceImpl implements IRespuestaService {

	@Autowired
	private IRespuestaRepository repository;
	
	@Autowired
	private IExamenFeignClient examenClient;
	
	@Override	
	public Iterable<Respuesta> saveAll(Iterable<Respuesta> respuestas) {
		return repository.saveAll(respuestas);
	}

	@Override
	public List<Respuesta> findRespuestaByAlumnoByExamen(Long alumnoId, Long examenId) { //Se modifica el metodo para obtener las respuestas por alumno y por examen de manera distribuida
		Examen examen = examenClient.obtenerExamenById(examenId);        		  	 //1. Se obtiene el examen mediante microservicio
		List<Pregunta> preguntas = examen.getPreguntas();							 //2. Se obnienen las preguntas a traves del examen
		List<Long> preguntaIds = preguntas.stream().map(p -> p.getId()).collect(Collectors.toList());    //3. Se obtienen los Ids de preguntas, modificando el List de preguntas mediane API Stream usando map
		List<Respuesta> respuestas = (List<Respuesta>) repository.findRespuestaByAlumnoByPreguntaIds(alumnoId, preguntaIds);   //4. Se obtienen las respuestas atraves del metodo del repositorio 
		respuestas = respuestas.stream().map(r ->{
			preguntas.forEach(p ->{													 //5. Ya que se obtienen las respuestas, las editamos y le agregamos el objeto pregunta: Si el id de cada pregunta es igual al id de cada respuesta,		
				if(p.getId() == r.getPreguntaId()) {  								 //y se le asigna el objeto pregunta p a la respuesta r que es la relacion
					r.setPregunta(p);
				}
			});
			return r;
		}).collect(Collectors.toList());
		
		return respuestas;															//6. Se retornan respuestas modificadas
	}

	@Override
	public Iterable<Long> findExamenesIdsWithRespuestasByAlumno(Long alumnoId) {
		List<Respuesta> respuestasAlumno = (List<Respuesta>) repository.findByAlumnoId(alumnoId); 	//1. Se obtienen las respuestas medinate el repositorio utilizando mongo
		List<Long> examenIds = Collections.emptyList();
		
		if(respuestasAlumno.size() > 0) {
			List<Long> preguntaIds = respuestasAlumno.stream().map(r -> r.getPreguntaId()).collect(Collectors.toList()); //2. Se combierten a una lista de preguntas ids
			examenIds = examenClient.obtenerExamenIdsPorPreguntasIdsRespondidas(preguntaIds);       //3. se va a buscar la lista de preguntas al microservicio-examenes utilizando feign de form distribuida
		}
		
		return examenIds;
	}

//	@Override
//	public List<Respuesta> findRespuestaByAlumnoByPreguntaIds(Long alumnoId, Iterable<Long> preguntaIds) {
//		return (List<Respuesta>) repository.findRespuestaByAlumnoByPreguntaIds(alumnoId, preguntaIds);
//	}

	@Override
	public Examen obtenerExamenById(Long id) {
		return examenClient.obtenerExamenById(id);
	}

	@Override
	public Iterable<Respuesta> findByAlumnoId(Long alumnoId) {
		return repository.findByAlumnoId(alumnoId);
	}

}

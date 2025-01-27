package com.formacionbdi.microservicios.app.respuestas.models.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.formacionbdi.microservicios.common.examenes.models.entity.Pregunta;
import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;
//Ya no es una entidad: por eso se eliminan las anotaciones anteriores. 

/** Ahora es un documento de mongoDB */
@Document(collection = "respuestas")
public class Respuesta {

	@Id
	private String id;  //El id Long se cambia por String por que es un tipo Alfanumerico y restriccion
	
	private String texto;
	
	@Transient
	private Pregunta pregunta;
	
	@Transient
	private Alumno alumno;
	
	private Long alumnoId;
	
	private Long preguntaId; //se agrega atributo preguntaId

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Pregunta getPregunta() {
		return pregunta;
	}

	public void setPregunta(Pregunta pregunta) {
		this.pregunta = pregunta;
	}

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public Long getAlumnoId() {
		return alumnoId;
	}

	public void setAlumnoId(Long alumnoId) {
		this.alumnoId = alumnoId;
	}

	public Long getPreguntaId() {
		return preguntaId;
	}

	public void setPreguntaId(Long preguntaId) {
		this.preguntaId = preguntaId;
	}

}

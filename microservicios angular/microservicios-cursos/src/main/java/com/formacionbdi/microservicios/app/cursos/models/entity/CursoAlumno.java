package com.formacionbdi.microservicios.app.cursos.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//Clase que Guarda el id de los alumnos: Se genera una tabla y no una tabla intermedia entre cursos y alumnos
@Entity
@Table(name="cursos_alumnos")
public class CursoAlumno {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//Un alumno solo puede estar registrado en un solo curso por eso se utiliz el unique
	@Column(name="alumno_id", unique = true)
	private Long alumnoId;
	
	@JsonIgnoreProperties(value = {"cursoAlumnos"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="curso_id")
	private Curso curso;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAlumnoId() {
		return alumnoId;
	}

	public void setAlumnoId(Long alumnoId) {
		this.alumnoId = alumnoId;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	} 
	
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CursoAlumno)) {
			return false;
		}
		CursoAlumno alu = (CursoAlumno) obj;
		return this.alumnoId != null && this.alumnoId.equals(alu.getAlumnoId());
	}
}

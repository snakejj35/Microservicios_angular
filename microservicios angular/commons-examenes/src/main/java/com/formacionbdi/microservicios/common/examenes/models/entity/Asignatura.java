package com.formacionbdi.microservicios.common.examenes.models.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "asignaturas")
public class Asignatura {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	
	@JsonIgnoreProperties(value= {"hijas", "handler", "hibernateLazyInitializer"})
	@ManyToOne(fetch = FetchType.LAZY)
	private Asignatura padre;
	
	@JsonIgnoreProperties(value = {"padre", "handler", "hibernateLazyInitializer"}, allowSetters = true)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "padre", cascade = CascadeType.ALL)
	private List<Asignatura> hijas;										   //Cascade: si se borra el padre tambien se eliminan los hijos
	
	public Asignatura() {
		this.hijas = new ArrayList<>();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Asignatura getPadre() {
		return padre;
	}

	public void setPadre(Asignatura padre) {
		this.padre = padre;
	}

	public List<Asignatura> getHijas() {
		return hijas;
	}

	public void setHijas(List<Asignatura> hijas) {
		this.hijas = hijas;
	}
		
}

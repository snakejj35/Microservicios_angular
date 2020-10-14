package com.formacionbdi.microservicios.common.examenes.models.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="examenes")
public class Examen {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Size(min = 5, max = 30)
	private String nombre;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_at")
	private Date createAt;	

	/** Un examen tiene muchas preguntas. mappedBy:Relacion inversa. Lazy carga la consulta con sus relaciones vi getters. Cascade elimina preguntas cuando se elimina un examen
	 *  orphanRemoval asignareferencia al examen en null y la llave foranea queda en null, por que no este relacionada a un examen**/
	@JsonIgnoreProperties(value = {"examen"}, allowSetters = true)   //Se ignora la relacion inversa y la la generacion de JSON pra esta propiedad. AllowSetters permite la asignacion y la ocultacion de examen
	@OneToMany(mappedBy = "examen", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Pregunta> preguntas;
	
	/**Muchos examenes a una asignatura */
	@JsonIgnoreProperties(value= {"handler", "hibernateLazyInitializer"})
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	private Asignatura asignaturaPadre;   //Se cambia el atributo asignatura a asiganturaPadre y la colunna asignatura_id desaparece
	
	/**Muchos examenes a una asignatura hija*/
	@JsonIgnoreProperties(value= {"handler", "hibernateLazyInitializer"})
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	private Asignatura asignaturaHija; //Se crea este campo de consulta para que al seleccionar el padre se muestre sus hijas
	
	@Transient
	private boolean respondido;
	
	public Examen() {
		this.preguntas = new ArrayList<>();
	}

	@PrePersist
	public void prePersist() {
		this.createAt = new Date();
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

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	
//	public List<Asignatura> getAsignaturas() {
//		return asignaturas;
//	}
//
//	public void setAsignaturas(List<Asignatura> asignaturas) {
//		this.asignaturas.clear();
//		asignaturas.forEach(this :: addAsignatura);
//	}
//	
//	public void addAsignatura(Asignatura asignatura) {
//		this.asignaturas.add(asignatura);
//		asignatura.setExamen(this);
//	}
//	
//	public void removeAsignatura(Asignatura asignatura) {
//		this.asignaturas.remove(asignatura);
//		asignatura.setExamen(null);
//	}

	public List<Pregunta> getPreguntas() {
		return preguntas;
	}

	//Porcada pregunta se asigna al examen del foreign key
	public void setPreguntas(List<Pregunta> preguntas) {
		this.preguntas.clear();  //Se reinicia las preguntas con las referencias de os objetos que se eliminaron e inicializar el arrayList una sola vez
		preguntas.forEach(this :: addPregunta); //Asigna las preguntas al examen y guarde la llave foranea. Lo mismo que: preguntas.forEach(p -> this.addPregunta(p));
	}
	
	//Por cada pregunta que se agrega, tambien se incluye el examen y se asigna e examen a la pregunta
	public void addPregunta(Pregunta pregunta) {
		this.preguntas.add(pregunta); //agrega la lista de preguntas al examen 
		pregunta.setExamen(this); //por cada pregunta que se agrega se debe incluir el examen en la pregunta. Con esto se establece la relacion inversa para guardar el id del examen y no quede en null 
	}
	
	public void removePregunta(Pregunta pregunta) {
		this.preguntas.remove(pregunta);
		pregunta.setExamen(null); //convierte la relacion en null para que se elimine
	}

	public boolean isRespondido() {
		return respondido;
	}

	public void setRespondido(boolean respondido) {
		this.respondido = respondido;
	}

	public Asignatura getAsignaturaPadre() {
		return asignaturaPadre;
	}

	public void setAsignaturaPadre(Asignatura asignaturaPadre) {
		this.asignaturaPadre = asignaturaPadre;
	}

	public Asignatura getAsignaturaHija() {
		return asignaturaHija;
	}

	public void setAsignaturaHija(Asignatura asignaturaHija) {
		this.asignaturaHija = asignaturaHija;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(!(obj instanceof Examen)) {
			return false;
		}
		Examen exa = (Examen) obj;
		return this.id != null && this.id.equals(exa.getId());
	}
	
}


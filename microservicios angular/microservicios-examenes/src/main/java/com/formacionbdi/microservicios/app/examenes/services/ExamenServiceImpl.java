package com.formacionbdi.microservicios.app.examenes.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacionbdi.microservicios.app.examenes.models.repository.IAsignaturaRepository;
import com.formacionbdi.microservicios.app.examenes.models.repository.IExamenRepository;
import com.formacionbdi.microservicios.common.examenes.models.entity.Asignatura;
import com.formacionbdi.microservicios.common.examenes.models.entity.Examen;
import com.formacionbdi.microservicios.common.services.CommonServiceImpl;

@Service
public class ExamenServiceImpl extends CommonServiceImpl<Examen, IExamenRepository> implements IExamenService {
    @Autowired
	private IAsignaturaRepository asignaturaRepository;
    
	@Override
	@Transactional(readOnly = true)
	public List<Examen> findByNombre(String value) {		
		return repository.findByNombre(value);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Iterable<Asignatura> findAllAsignaturas() {
		return asignaturaRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<Long> findExamenesIdsWithRespuestasByPreguntasIds(Iterable<Long> preguntasIds) {
		return repository.findExamenesIdsWithRespuestasByPreguntasIds(preguntasIds);
	}

}

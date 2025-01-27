package com.formacionbdi.microservicios.common.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICommonService<E> {
	
	public Iterable<E> findAll();
	
	//Paginable, mediante el offset y el limit: limit=# de elementos por pagina. OffSet: se calcula atraves de la pagina actual.
	public Page<E> findAll(Pageable pageable);
	
	public Optional<E> finById(Long id);
	
	public E save(E entity);
	
	public void deleteById(Long id);

}

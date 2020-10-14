package com.formacionbdi.microservicios.app.cursos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
@EntityScan({"com.formacionbdi.microservicios.common.examenes.models.entity",
		     "com.formacionbdi.microservicios.app.cursos.models.entity"}) //Se quita "com.formacionbdi.microservicios.commons.alumnos.models.entity" por que es @Trasient en Cursos y no es necesario escanearse
public class MicroserviciosCursosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviciosCursosApplication.class, args);
	}

}

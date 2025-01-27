package com.formacionbdi.microservicios.app.respuestas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;



//Se elimina el @EntityScan por que ya no son entidades de JPA, ahora solo son clases para MongoDB 
/** @EntityScan({"com.formacionbdi.microservicios.app.respuestas.models.entity"
            ,"com.formacionbdi.microservicios.common.examenes.models.entity"})  // se quita el "com.formacionbdi.microservicios.commons.alumnos.models.entity" por que es @Transient en Respuestas y no es necesario
*/
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})   //Deshailita la configuracion automatica del datasource. Se agrega para evitar conexiones de Spring Data JPA, ya que tiene las dependencias de commons-alumnos y commons-examenes
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class MicroserviciosRespuestasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviciosRespuestasApplication.class, args);
	}

}

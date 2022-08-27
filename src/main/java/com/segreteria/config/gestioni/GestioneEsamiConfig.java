package com.segreteria.config.gestioni;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.segreteria.model.wrapper.GestioneEsami;

@Configuration
public class GestioneEsamiConfig {

	@Bean
	public GestioneEsami generaEsami(){
		return new GestioneEsami();
	}
}

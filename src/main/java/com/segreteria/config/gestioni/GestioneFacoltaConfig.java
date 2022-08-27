package com.segreteria.config.gestioni;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.segreteria.model.wrapper.GestioneFacolta;

@Configuration
public class GestioneFacoltaConfig {

	@Bean
	public GestioneFacolta generaFacolta(){
		return new GestioneFacolta();
	}
}

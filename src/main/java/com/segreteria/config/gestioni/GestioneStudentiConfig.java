package com.segreteria.config.gestioni;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.segreteria.model.wrapper.GestioneStudenti;

@Configuration
public class GestioneStudentiConfig {

	@Bean
	public GestioneStudenti generaStudenti(){
		return new GestioneStudenti();
	}
}

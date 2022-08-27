package com.segreteria.config.gestioni;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.segreteria.model.wrapper.GestioneCorsi;

@Configuration
public class GestioneCorsiConfig {

	@Bean
	public GestioneCorsi generaCorsi() {
		return new GestioneCorsi();
	}
}

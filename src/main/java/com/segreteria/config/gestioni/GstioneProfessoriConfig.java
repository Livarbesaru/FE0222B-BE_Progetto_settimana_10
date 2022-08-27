package com.segreteria.config.gestioni;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.segreteria.model.wrapper.GestioneProfessori;

@Configuration
public class GstioneProfessoriConfig {
	@Bean
	public GestioneProfessori generaProfessori(){
		return new GestioneProfessori();
	}
}

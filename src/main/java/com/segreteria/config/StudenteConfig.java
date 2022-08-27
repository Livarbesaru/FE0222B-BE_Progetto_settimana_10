package com.segreteria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.segreteria.model.Studente;

@Configuration
public class StudenteConfig {

	@Bean
	public Studente studente() {
		return new Studente();
	}
}

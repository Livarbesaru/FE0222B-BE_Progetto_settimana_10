package com.segreteria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.segreteria.model.Facolta;

@Configuration
public class FacoltaConfig {

	@Bean
	public Facolta facolta() {
		return new Facolta();
	}
}

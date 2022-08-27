package com.segreteria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.segreteria.model.Esame;

@Configuration
public class EsameConfig {

	@Bean
	public Esame esame() {
		return new Esame();
	}
}

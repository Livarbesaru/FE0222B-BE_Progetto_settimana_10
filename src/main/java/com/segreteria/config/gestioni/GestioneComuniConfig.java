package com.segreteria.config.gestioni;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.segreteria.model.wrapper.GestioneCitta;

@Configuration
public class GestioneComuniConfig {
	
	@Bean
	public GestioneCitta generaCitta() {
		return new GestioneCitta();
	}
}

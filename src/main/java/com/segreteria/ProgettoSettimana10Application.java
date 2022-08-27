package com.segreteria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.segreteria.model.Universita;

@SpringBootApplication
public class ProgettoSettimana10Application {

	public static void main(String[] args) {
		SpringApplication.run(ProgettoSettimana10Application.class, args);
		new Universita();
	}

}

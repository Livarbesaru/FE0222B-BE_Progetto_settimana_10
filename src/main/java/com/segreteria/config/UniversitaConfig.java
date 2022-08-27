package com.segreteria.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.segreteria.model.Universita;
import com.segreteria.model.wrapper.GestioneCorsi;
import com.segreteria.model.wrapper.GestioneEsami;
import com.segreteria.model.wrapper.GestioneFacolta;
import com.segreteria.model.wrapper.GestioneProfessori;
import com.segreteria.model.wrapper.GestioneStudenti;

@Configuration
public class UniversitaConfig {

	@Bean
	public Universita universita() throws NumberFormatException, IOException {
		Universita uni=new Universita();
		/*uni.setFacolta(new GestioneFacolta().prendiLista());
		uni.setProfessori(new GestioneProfessori().prendiLista());
		uni.setEsami(new GestioneEsami().prendiLista());
		uni.setCorsi(new GestioneCorsi().prendiLista());
		uni.setStudenti(new GestioneStudenti().prendiLista());*/
		return uni;
	}
}

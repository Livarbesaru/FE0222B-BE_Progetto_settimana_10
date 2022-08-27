package com.segreteria.util;

import java.time.LocalDate;
import java.util.Date;

import com.segreteria.model.CorsoDiLaurea;
import com.segreteria.model.Studente;
import com.segreteria.model.residenza.Residenza;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class BuilderStudente {
	private Long matricola;
	private String nome;
	private String cognome;
	private Date dataN;
	private String email;
	private Residenza residenza;
	private CorsoDiLaurea corso;
		
		public BuilderStudente(@NonNull Long matricola,@NonNull String nome,@NonNull String cognome) {
			this.nome=nome;
			this.cognome=cognome;
			this.matricola=matricola;
		}
		
		public BuilderStudente dataNascita(@NonNull Date data) 
		{	this.dataN=data;	return this;	}
		
		public BuilderStudente email(@NonNull String email)
		{ this.email=email; return this;}
		
		
		public BuilderStudente corso(CorsoDiLaurea corso) 
		{	this.corso=corso; return this;}
		
		public BuilderStudente residenza(@NonNull Residenza residenza)
		{ this.residenza=residenza;; return this;}
		
		
		public Studente build() {
			return new Studente(this);
		}
}
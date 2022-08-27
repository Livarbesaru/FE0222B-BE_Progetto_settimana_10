package com.segreteria.util;

import java.util.Date;

import com.segreteria.model.Professore;
import com.segreteria.model.residenza.Residenza;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class BuilderProfessore {
		private Long matricola;
		private String nome;
		private String cognome;
		private Date dataN;
		private String email;
		private Residenza residenza;
			
			public BuilderProfessore(@NonNull Long matricola,@NonNull String nome,@NonNull String cognome) {
				this.nome=nome;
				this.cognome=cognome;
				this.matricola=matricola;
			}
			
			public BuilderProfessore dataNascita(@NonNull Date data) 
			{	this.dataN=data;	return this;	}
			
			public BuilderProfessore email(@NonNull String email)
			{ this.email=email; return this;}
			
			public BuilderProfessore residenza(@NonNull Residenza residenza)
			{ this.residenza=residenza;; return this;}
			
			
			public Professore build() {
				return new Professore(this);
			}
}

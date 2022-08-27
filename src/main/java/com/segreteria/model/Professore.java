package com.segreteria.model;

import java.util.Date;

import com.segreteria.model.residenza.Residenza;
import com.segreteria.util.BuilderProfessore;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class Professore {
		private Long matricola;
		private String nome;
		private String cognome;
		private Date dataN;
		private String email;
		private Residenza residenza;
		
		public Professore(@NonNull BuilderProfessore builder){
			this.nome=builder.getNome();
			this.cognome=builder.getCognome();
			this.dataN=builder.getDataN();
			this.email=builder.getEmail();
			this.matricola=builder.getMatricola();
			this.residenza=builder.getResidenza();
		}
		
		public Professore(){
		}
		
		public String getNominativo() {
			return this.nome+" "+this.cognome;
		}
		
		public String stampaData() {
			String[] s=this.dataN.toString().split(" ");
			String data=s[5]+"-"+s[1]+"-"+s[2];
			return data;
		}

		public void setDatiProfessore(Professore pf) {
			this.nome=pf.nome;
			this.cognome=pf.cognome;
			this.dataN=pf.dataN;
			this.email=pf.email;
			this.residenza=pf.residenza;
		}
	
}

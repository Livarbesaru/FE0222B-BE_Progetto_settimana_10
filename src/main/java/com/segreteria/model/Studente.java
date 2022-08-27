package com.segreteria.model;

import java.util.Date;

import com.segreteria.model.residenza.Residenza;
import com.segreteria.util.BuilderStudente;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class Studente {
	private Long matricola;
	private String nome;
	private String cognome;
	private Date dataN;
	private String email;
	private Residenza residenza;
	private CorsoDiLaurea corso;
	
	public Studente(@NonNull BuilderStudente builder){
		this.nome=builder.getNome();
		this.cognome=builder.getCognome();
		this.dataN=builder.getDataN();
		this.email=builder.getEmail();
		this.matricola=builder.getMatricola();
		this.residenza=builder.getResidenza();
		this.corso=builder.getCorso();
	}
	
	public Studente(){
	}
	
	public String getNominativo() {
		return this.nome+" "+this.cognome;
	}
	
	public String stampaData() {
		String[] s=this.dataN.toString().split(" ");
		String data=s[5]+"-"+s[1]+"-"+s[2];
		return data;
	}
	
	public void setDatiStudente(Studente studente) {
		this.nome=studente.nome;
		this.cognome=studente.cognome;
		this.dataN=studente.dataN;
		this.email=studente.email;
		this.residenza=studente.residenza;
		this.corso=studente.corso;
	}
}

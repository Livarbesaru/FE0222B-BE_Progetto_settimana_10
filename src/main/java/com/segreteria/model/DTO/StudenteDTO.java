package com.segreteria.model.DTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudenteDTO {
	private Long matricola;
	
	@Size(min=2)
	private String nome;
	@Size(min=2)
	private String cognome;
	@Size(min=4)
	private String dataN;
	@Email 
	private String email;
	private int idCorso;
	@Size(min=3)
	private String capCitta;
	@Size(min=2)
	private String via;
	private int civico;
	
	public StudenteDTO(){}
	
	public String getNominativo() {
		return this.nome+" "+this.cognome;
	}
	
	public String stampaData() {
		String[] s=this.dataN.toString().split(" ");
		String data=s[5]+"-"+s[1]+"-"+s[2];
		return data;
	}

}

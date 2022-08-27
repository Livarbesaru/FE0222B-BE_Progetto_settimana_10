package com.segreteria.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class CorsoDiLaurea{
	private int idCorso;
	private int numEsami;
	private Set<Esame> esami=new HashSet<Esame>();
	private @NonNull String nome;
	private @NonNull Facolta facolta;
	
	public CorsoDiLaurea(int idCorso, int numEsami, @NonNull String nome) {
		super();
		this.idCorso = idCorso;
		this.numEsami = numEsami;
		this.nome = nome;
	}
	
	public void setIdCorso(int id) {
		this.idCorso=id;
	}
	public void setEsami(@NonNull Set<Esame> esami) {
		this.esami=esami;
		this.numEsami=esami.size();
	}
	
	public void setDatiCorso(CorsoDiLaurea corso) {
		this.numEsami=corso.numEsami;
		this.esami=corso.esami;
		this.nome=corso.nome;
		this.facolta=corso.facolta;
	}
}

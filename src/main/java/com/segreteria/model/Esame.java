package com.segreteria.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Esame {
	private Long id;
	private String nome;
	private Set<Professore> professori=new HashSet<>(2);
	
	public Esame(Long id,String nome){
		this.id=id;
		this.nome=nome;
	}
	
	public ArrayList<Professore> listaProfessori() {
		ArrayList<Professore> pl=new ArrayList<>();
		for(Professore p:professori) {
			pl.add(p);
		}
		return pl;
	}
	
	public void setDatiEsame(Esame esame) {
		this.nome=esame.nome;
		this.professori=esame.professori;
	}
}

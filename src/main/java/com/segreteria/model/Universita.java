package com.segreteria.model;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeansException;

import com.segreteria.model.wrapper.GestioneCorsi;
import com.segreteria.model.wrapper.GestioneEsami;
import com.segreteria.model.wrapper.GestioneFacolta;
import com.segreteria.model.wrapper.GestioneProfessori;
import com.segreteria.model.wrapper.GestioneStudenti;

import lombok.Setter;

@Setter
public class Universita {

	Set<Facolta> facolta=new HashSet<>();
	Set<CorsoDiLaurea> corsi=new HashSet<>();
	Set<Studente> studenti=new HashSet<>();
	Set<Esame> esami=new HashSet<>();
	Set<Professore> professori=new HashSet<>();
	
	
	public Set<Facolta> getFacolta() throws NumberFormatException, IOException{
		this.facolta=new GestioneFacolta().prendiLista();
		return this.facolta;
	}
	
	public Set<CorsoDiLaurea> getCorsi() throws NumberFormatException, IOException{
		this.corsi=new GestioneCorsi().prendiLista();
		return this.corsi;
	}
	
	public Set<Studente> getStudenti() throws BeansException, NumberFormatException, IOException{
		this.studenti=new GestioneStudenti().prendiLista();
		return this.studenti;
	}
	
	public Set<Esame> getEsami() throws NumberFormatException, IOException{
		this.esami=new GestioneEsami().prendiLista();
		return this.esami;
	}
	
	public Set<Professore> getProfessori() throws BeansException, NumberFormatException, IOException{
		this.professori=new GestioneProfessori().prendiLista();
		return this.professori;
	}
}

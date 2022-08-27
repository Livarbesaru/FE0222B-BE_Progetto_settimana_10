package com.segreteria.model.wrapper;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.BeansException;

import com.segreteria.model.Professore;
import com.segreteria.util.scritturaLettura.ProfessoreFile;

import lombok.Getter;

@Getter
public class GestioneProfessori {
	private static Set<Professore> listaProfessori=null;
	
	public Set<Professore> prendiLista() throws BeansException, NumberFormatException, IOException {
		listaProfessori=ProfessoreFile.generaProfessori();
		return this.listaProfessori;
	}
	
	public void scrittura(Professore professore) throws NumberFormatException, IOException  {
		ProfessoreFile.scrittura(professore);
	}
	
	public void modifica(Set<Professore> professori) throws NumberFormatException, IOException  {
		ProfessoreFile.modifica(professori);
	}
}
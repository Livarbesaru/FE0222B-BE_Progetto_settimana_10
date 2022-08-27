package com.segreteria.model.wrapper;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.BeansException;

import com.segreteria.model.Studente;
import com.segreteria.model.DTO.StudenteDTO;
import com.segreteria.util.scritturaLettura.FacoltaFile;
import com.segreteria.util.scritturaLettura.StudenteFile;

import lombok.Getter;

@Getter
public class GestioneStudenti {
	private static Set<Studente> listaStudenti=null;
	
	public Set<Studente> prendiLista() throws BeansException, NumberFormatException, IOException {
		listaStudenti=StudenteFile.generaStudenti();
		return this.listaStudenti;
	}
	
	public void scrittura(Studente studente) throws NumberFormatException, IOException  {
		StudenteFile.scrittura(studente);
	}
	
	public void modifica(Set<Studente> studenti) throws NumberFormatException, IOException  {
		StudenteFile.modifica(studenti);
	}
}
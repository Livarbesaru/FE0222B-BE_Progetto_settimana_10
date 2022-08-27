package com.segreteria.model.wrapper;

import java.io.IOException;
import java.util.Set;

import com.segreteria.model.CorsoDiLaurea;
import com.segreteria.model.DTO.CorsoDiLaureaDTO;
import com.segreteria.util.scritturaLettura.CittaFile;
import com.segreteria.util.scritturaLettura.CorsoFile;

import lombok.Getter;
@Getter
public class GestioneCorsi {
	private static Set<CorsoDiLaurea> listaCorsi=null;
	
	public Set<CorsoDiLaurea> prendiLista() throws NumberFormatException, IOException {
		listaCorsi=CorsoFile.generaCorsi();
		return this.listaCorsi;
	}
	
	public void scrittura(CorsoDiLaurea corso) throws NumberFormatException, IOException {
		CorsoFile.scrittura(corso);
	}
	
	public void modifica(Set<CorsoDiLaurea> corso) throws NumberFormatException, IOException {
		CorsoFile.modficia(corso);
	}
}

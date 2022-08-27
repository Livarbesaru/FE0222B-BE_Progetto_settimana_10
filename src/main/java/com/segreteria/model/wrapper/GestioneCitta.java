package com.segreteria.model.wrapper;

import java.io.IOException;
import java.util.Set;

import com.segreteria.model.residenza.Citta;
import com.segreteria.util.scritturaLettura.CittaFile;

import lombok.Getter;

@Getter
public class GestioneCitta {
	private static Set<Citta> listaCitta=null;
	
	public Set<Citta> prendiLista() throws NumberFormatException, IOException {
		listaCitta=CittaFile.generaCitta();
		return this.listaCitta;
	}
}

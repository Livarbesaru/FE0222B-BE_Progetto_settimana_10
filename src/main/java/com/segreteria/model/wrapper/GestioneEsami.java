package com.segreteria.model.wrapper;

import java.io.IOException;
import java.util.Set;

import com.segreteria.model.Esame;
import com.segreteria.model.Facolta;
import com.segreteria.util.scritturaLettura.CorsoFile;
import com.segreteria.util.scritturaLettura.EsameFile;
import com.segreteria.util.scritturaLettura.FacoltaFile;

import lombok.Getter;

@Getter
public class GestioneEsami {

	private static Set<Esame> listaEsami=null;
	
	public Set<Esame> prendiLista() throws NumberFormatException, IOException {
		listaEsami=EsameFile.generaEsami();
		return this.listaEsami;
	}
	
	public void scrittura(Esame esame) throws NumberFormatException, IOException {
		EsameFile.scrittura(esame);
	}
	
	public void modifica(Set<Esame> esami) throws NumberFormatException, IOException {
		EsameFile.modifica(esami);
	}
}

package com.segreteria.model.wrapper;

import java.io.IOException;
import java.util.Set;

import com.segreteria.model.Facolta;
import com.segreteria.util.scritturaLettura.EsameFile;
import com.segreteria.util.scritturaLettura.FacoltaFile;

import lombok.Getter;

@Getter
public class GestioneFacolta {
	private static Set<Facolta> listaFacolta=null;
	
	public Set<Facolta> prendiLista() throws NumberFormatException, IOException {
		listaFacolta=FacoltaFile.generaFacolta();
		return this.listaFacolta;
	}
	
	public void scrittura(Facolta facolta) throws NumberFormatException, IOException {
		FacoltaFile.scrittura(facolta);
	}
	public void modifica(Set<Facolta> facolta) throws NumberFormatException, IOException {
		FacoltaFile.modifica(facolta);
	}
}

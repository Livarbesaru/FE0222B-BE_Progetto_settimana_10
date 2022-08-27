package com.segreteria.util.scritturaLettura;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import com.segreteria.model.residenza.Citta;

/*Qui vengono letti i dati di tutti i comuni italiani,
 * e renderli cosi disponibili durante la scelta della residenza*/
public class CittaFile {
	public static Set<Citta> generaCitta() throws NumberFormatException, IOException {
		Set<Citta> citta=new HashSet<>();
		BufferedReader in = null;
		try {
			FileInputStream is = new FileInputStream("src\\main\\resources\\fakedb\\listacomuni.txt");
			in = new BufferedReader(new InputStreamReader(is));
			String line;
			while((line = in.readLine()) != null) {
				String[] s=line.toString().split(";");
			   citta.add(new Citta(s[1],s[5]));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return citta;
	}
}

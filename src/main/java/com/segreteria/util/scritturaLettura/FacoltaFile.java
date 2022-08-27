package com.segreteria.util.scritturaLettura;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.segreteria.config.UniversitaConfig;
import com.segreteria.config.gestioni.GestioneComuniConfig;
import com.segreteria.config.gestioni.GestioneFacoltaConfig;
import com.segreteria.model.Facolta;
import com.segreteria.model.Universita;

/*Utilizzato per la scrittura/lettura del file(fakedb) per la facolta*/
public class FacoltaFile {
	private final static ApplicationContext ctx=new AnnotationConfigApplicationContext(UniversitaConfig.class);

	/*Utilizzato per l'aggiunta della singola facolta*/
	public static void scrittura(Facolta facolta) throws NumberFormatException, IOException {
		String f="";
		File file=new File("src\\main\\resources\\fakedb\\listafacolta.txt");
		Set<Facolta> fac=generaFacolta();
		fac.add(facolta);
		for(Facolta item:fac) {
			f+=(item.getId()+";"
					+item.getNomeFacolta())+";"+"\n";
		}
		try {
			((Universita)ctx.getBean("universita")).setFacolta(fac);
			FileUtils.writeStringToFile(file, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*Utilizzato per l'eliminazione o modifica di alcune facolta nel file di memoria*/
	public static void modifica(Set<Facolta> facolta) {
		String f="";
		File file=new File("src\\main\\resources\\fakedb\\listafacolta.txt");
		Set<Facolta> fac=facolta;
		for(Facolta item:fac) {
			f+=(item.getId()+";"
					+item.getNomeFacolta())+";"+"\n";
		}
		try {
			((Universita)ctx.getBean("universita")).setFacolta(fac);
			FileUtils.writeStringToFile(file, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*Utilizzato per la lettura dei dati in memoria per convertire ad un set di facolta*/
	public static Set<Facolta> generaFacolta() throws NumberFormatException, IOException {
		HashSet<Facolta> facolta=new HashSet<>();
		BufferedReader in = null;
		try {
			FileInputStream is = new FileInputStream("src\\main\\resources\\fakedb\\listafacolta.txt");
			in = new BufferedReader(new InputStreamReader(is));
			String line;
			while((line = in.readLine()) != null) {
				String[] s=line.toString().split(";");
				if(s.length>1) {
					Facolta fac=new Facolta(Integer.parseInt(s[0]),s[1]);
					facolta.add(fac);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return facolta;
	}
}

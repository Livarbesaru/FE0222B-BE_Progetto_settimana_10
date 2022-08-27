package com.segreteria.util.scritturaLettura;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.segreteria.config.UniversitaConfig;
import com.segreteria.model.Esame;
import com.segreteria.model.Facolta;
import com.segreteria.model.Professore;
import com.segreteria.model.Universita;
import com.segreteria.model.wrapper.GestioneProfessori;

/*Utilizzato per la scrittura e lettura dei dati dal file txt*/
public class EsameFile {	
	
	private final static ApplicationContext ctx=new AnnotationConfigApplicationContext(UniversitaConfig.class);
	
	/*utilizzato per l'aggiunta del singolo esame alla lista di dati*/
	public static void scrittura(Esame esame) throws NumberFormatException, IOException {
		String f="";
		File file=new File("src\\main\\resources\\fakedb\\listaesami.txt");
		Set<Esame> esami=generaEsami();
		esami.add(esame);
		for(Esame e:esami) {
			f+=(e.getId()+";"
			+e.getNome()+";");
			for(Professore p:e.getProfessori()) {
				f+=p.getMatricola()+";";
			}
			f+="\n";
		}
		try {
			((Universita)ctx.getBean("universita")).setEsami(esami);
			FileUtils.writeStringToFile(file, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*Utilizzato per l'eliminazione o modifica di alcuni esami, l'intero file viene sovrascritto*/
	public static void modifica(Set<Esame> esami) {
		String f="";
		File file=new File("src\\main\\resources\\fakedb\\listaesami.txt");
		Set<Esame> esa=esami;
		for(Esame e:esami) {
			f+=(e.getId()+";"
			+e.getNome()+";");
			for(Professore p:e.getProfessori()) {
				f+=p.getMatricola()+";";
			}
			f+="\n";
		}
		try {
			((Universita)ctx.getBean("universita")).setEsami(esa);
			FileUtils.writeStringToFile(file, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*utilizzato per la lettura del file contenente la lista di esami presenti nell'universita*/
	public static Set<Esame> generaEsami() throws NumberFormatException, IOException{
		HashSet<Esame> esami=new HashSet<>();
		BufferedReader in = null;
		try {
			FileInputStream is = new FileInputStream("src\\main\\resources\\fakedb\\listaesami.txt");
			in = new BufferedReader(new InputStreamReader(is));
			String line;
			/*L'intero file viene letto linea per linea*/
			while((line = in.readLine()) != null) {
				/*la linea viene convertita in un array di stringhe*/
				String[] s=line.toString().split(";");
				if(s.length>1) {
					/*se l'array contiene piu di un dato allora si istanzia un esame
					 * questo prenderà i dati presenti nella linea in lettura*/
					Esame esame=new Esame(Long.parseLong(s[0]),s[1]);
					Set<Professore> professori=new HashSet<>();
					
					/*Per i professore/i dell'esame, si va a ricercare i professori
					 * presenti nella lista professori e cerca quelli con l'indice corrispondenti
					 * a quelli in lettura*/
					for(Professore p:(((Universita)ctx.getBean("universita")).getProfessori())) {
						if(p.getMatricola()==Long.parseLong(s[2])) {
							professori.add(p);
						}
						if(s.length>3 && p.getMatricola()==Long.parseLong(s[3])) {
							professori.add(p);
						}
					}
					esame.setProfessori(professori);
					esami.add(esame);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return esami;
	}
}

package com.segreteria.util.scritturaLettura;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.segreteria.config.UniversitaConfig;
import com.segreteria.model.CorsoDiLaurea;
import com.segreteria.model.Esame;
import com.segreteria.model.Facolta;
import com.segreteria.model.Universita;
import com.segreteria.model.wrapper.GestioneEsami;
import com.segreteria.model.wrapper.GestioneFacolta;

/*Qui si gestisce la lettura e scrittura dei file che simulano 
 * la persistenza dei dati in un database*/
public class CorsoFile {
	private final static ApplicationContext ctx=new AnnotationConfigApplicationContext(UniversitaConfig.class);
	
	/*In questo metodo viene sovrascritto il file contenete i dati dei corsi
	 * aggiungendogli il corso che gli viene passato come paramentro*/
	public static void scrittura(CorsoDiLaurea corso) throws NumberFormatException, IOException {
		String f="";
		File file=new File("src\\main\\resources\\fakedb\\listacorsi.txt");
		Set<CorsoDiLaurea> corsi=generaCorsi();
		corsi.add(corso);
		for(CorsoDiLaurea c:corsi) {
			f+=(c.getIdCorso()+";"+
					c.getNumEsami()+";");
			for(Esame esame:c.getEsami()) {
				f+=(esame.getId()+";");
			}
			f+=(c.getNome()+";"+
					c.getFacolta().getId()+";"+"\n");
		}
		try {
			((Universita)ctx.getBean("universita")).setCorsi(corsi);
			FileUtils.writeStringToFile(file, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*Metodo utilizzato principalmente per l'eliminazione di corsi o modifica*/
	public static void modficia(Set<CorsoDiLaurea> corsi) throws NumberFormatException, IOException {
		String f="";
		File file=new File("src\\main\\resources\\fakedb\\listacorsi.txt");
		Set<CorsoDiLaurea> corsiL=corsi;
		for(CorsoDiLaurea c:corsiL) {
			f+=(c.getIdCorso()+";"+
					c.getNumEsami()+";");
			for(Esame esame:c.getEsami()) {
				f+=(esame.getId()+";");
			}
			f+=(c.getNome()+";"+
					c.getFacolta().getId()+";"+"\n");
		}
		try {
			((Universita)ctx.getBean("universita")).setCorsi(corsiL);
			FileUtils.writeStringToFile(file, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*Metodo utilizzato per la lettura dei corsi sul file*/
	public static Set<CorsoDiLaurea> generaCorsi() throws NumberFormatException, IOException {
		HashSet<CorsoDiLaurea> corsi=new HashSet<>();
		BufferedReader in = null;
		try {
			FileInputStream is = new FileInputStream("src\\main\\resources\\fakedb\\listacorsi.txt");
			in = new BufferedReader(new InputStreamReader(is));
			String line;
			/*I dati(linee) presenti sul file vengono lette una ad una*/
			while((line = in.readLine()) != null) {
				String[] s=line.toString().split(";");
				/* se la linea una volta divisa in un array presenta piu' di un singolo dato
				 * allora risulta non avere problemi di scrittura e viene quindi letta*/
				if(s.length>1) {
					/*Viene istanziato un corso con dati uguali a quelli presenti nella linea in lettura*/
					CorsoDiLaurea corso=new CorsoDiLaurea(
							Integer.parseInt(s[0]),
							Integer.parseInt(s[1]),
							s[Integer.parseInt(s[1])+2]);
					
					/*Successivamente viene a crearsi un set di esami che corrispondono agli esami del corso
					 * in lettura
					 * */
					HashSet<Esame> esami=new HashSet<>();
					for(int i=0;i<s.length;i++) {
						/*Si leggono i dati(indici di esami) presenti nella linea di lettura dall'indice dell'array
						 * maggiore all'uno (corrispondente al numero di esami presente nel corso), 
						 * fino allo stesso numero +2 (questo range di valori contiene gli indici per gli esami)*/
						if(i>1 && i<((Integer.parseInt(s[1]))+2)) {
							int c=i;//mettere i direttamente nell filter da errore
							
							/*vengono letti gli esami presenti nell'universita, quelli corrispondenti alla ricerca
							 * vengono aggiunti al corso*/
							esami.add(((Universita)ctx.getBean("universita")).getEsami().stream()
									.filter(x->x.getId()==Long.parseLong(s[c]))
									.collect(Collectors.toList()).get(0)
									);
						}
					}
					int baseIndx=Integer.parseInt(s[1])+3;
					/*Viene ricercata la facolta a cui il corso appartiene*/
					Facolta fac=((Universita)ctx.getBean("universita")).getFacolta().stream()
							.filter(x->x.getId()==(Integer.parseInt(
							s[baseIndx]))).collect(Collectors.toList()).get(0);
					corso.setEsami(esami);
					corso.setFacolta(fac);
					corsi.add(corso);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return corsi;
	}
}

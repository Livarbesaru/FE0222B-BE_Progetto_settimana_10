package com.segreteria.util.scritturaLettura;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.segreteria.config.UniversitaConfig;
import com.segreteria.config.gestioni.GestioneComuniConfig;
import com.segreteria.model.CorsoDiLaurea;
import com.segreteria.model.Professore;
import com.segreteria.model.Studente;
import com.segreteria.model.Universita;
import com.segreteria.model.residenza.Citta;
import com.segreteria.model.residenza.Indirizzo;
import com.segreteria.model.residenza.Residenza;
import com.segreteria.model.wrapper.GestioneCitta;
import com.segreteria.model.wrapper.GestioneCorsi;
import com.segreteria.util.BuilderProfessore;
import com.segreteria.util.BuilderStudente;

/*Utilizzato per la scrittura/lettura del file(fakedb) per la lista di studenti*/
public class ProfessoreFile {
	private final static ApplicationContext ctx=new AnnotationConfigApplicationContext(GestioneComuniConfig.class);
	private final static ApplicationContext ctx2=new AnnotationConfigApplicationContext(UniversitaConfig.class);
		
		/*Utilizzato per l'aggiunta del singolo professore al file contenente la lista*/
		public static void scrittura(Professore prof) throws BeansException, NumberFormatException, IOException {
			String f="";
			Set<Professore> professori=generaProfessori();
			professori.add(prof);
			File file=new File("src\\main\\resources\\fakedb\\listaprofessori.txt");
			for(Professore s:professori) {
				f+=(s.getMatricola()+";"
				+s.getNome()+";"
				+s.getCognome()+";"
				+s.getDataN().getYear()+"-"+s.getDataN().getMonth()+"-"+s.getDataN().getDay()+";"
				+s.getEmail()+";"
				+s.getResidenza().getCitta().getCap()+";"
				+s.getResidenza().getIndirizzo().getVia()+";"
				+s.getResidenza().getIndirizzo().getCivico()+";"+"\n");
			}
			try {
				((Universita)ctx2.getBean("universita")).setProfessori(professori);
				FileUtils.writeStringToFile(file, f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/*Utilizzato per la modifica o eliminazione di alcuni stidenti dalla lista presente nel file*/
		public static void modifica(Set<Professore> professori) throws BeansException, NumberFormatException, IOException {
			String f="";
			Set<Professore> profs=professori;
			File file=new File("src\\main\\resources\\fakedb\\listaprofessori.txt");
			for(Professore p:profs) {
				f+=(p.getMatricola()+";"
				+p.getNome()+";"
				+p.getCognome()+";"
				+p.getDataN().getYear()+"-"+p.getDataN().getMonth()+"-"+p.getDataN().getDay()+";"
				+p.getEmail()+";"
				+p.getResidenza().getCitta().getCap()+";"
				+p.getResidenza().getIndirizzo().getVia()+";"
				+p.getResidenza().getIndirizzo().getCivico()+";"+"\n");
			}
			try {
				((Universita)ctx2.getBean("universita")).setProfessori(profs);
				FileUtils.writeStringToFile(file, f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/*Utilizzato per la lettura dei dati nel file*/
		public static Set<Professore> generaProfessori() throws BeansException, NumberFormatException, IOException {
			HashSet<Professore> professori=new HashSet<>();
			BufferedReader in = null;
			try {
				FileInputStream is = new FileInputStream("src\\main\\resources\\fakedb\\listaprofessori.txt");
				in = new BufferedReader(new InputStreamReader(is));
				String line;
				/*il file viene letto linea per linea, ed ognuna di esse viene convertita
				 * in un array di stringhe*/
				while((line = in.readLine()) != null) {
					String[] s=line.toString().split(";");
					/*Se l'array contiente piu di un valore allora viene giudicato come valido*/
					if(s.length>1) {
						/*si cerca a quale citta nel database lo studente abbia la residenza*/
						Citta citta=((GestioneCitta)ctx.getBean("generaCitta")).prendiLista()
								.stream().filter(x->x.getCap().equals(s[5])).collect(Collectors.toList()).get(0);
						
						/*la data dello studente viene settata con i setDate/Year/Month in quanto unico metodo
						 * insegnatoci per settare le date*/
						String[] dataString=s[3].split("-");
						Date dataS=new Date();
						dataS.setYear(Integer.parseInt(dataString[0]));
						dataS.setMonth(Integer.parseInt(dataString[1]));
						dataS.setDate(Integer.parseInt(dataString[2]));
						
						/*alla fine di crea un istanza di un professore attraverso un builder che prende
						 * i dati fino ad ora raccolti e li utilizza per generare il professore*/
						Professore prof=new BuilderProfessore(Long.parseLong(s[0]),s[1],s[2])
								.dataNascita(dataS)
								.email(s[4])
								.residenza(new Residenza(citta, new Indirizzo(s[6], Integer.parseInt(s[7]))))
								.build();
						professori.add(prof);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			return professori;
		}
}

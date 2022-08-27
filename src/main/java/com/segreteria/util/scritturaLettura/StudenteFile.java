package com.segreteria.util.scritturaLettura;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.segreteria.config.UniversitaConfig;
import com.segreteria.config.gestioni.GestioneComuniConfig;
import com.segreteria.model.CorsoDiLaurea;
import com.segreteria.model.Studente;
import com.segreteria.model.Universita;
import com.segreteria.model.DTO.StudenteDTO;
import com.segreteria.model.residenza.Citta;
import com.segreteria.model.residenza.Indirizzo;
import com.segreteria.model.residenza.Residenza;
import com.segreteria.model.wrapper.GestioneCitta;
import com.segreteria.model.wrapper.GestioneCorsi;
import com.segreteria.util.BuilderStudente;

/*Utilizzato per la scrittura e lettura del file(fakedb) contenente la lista dei studenti*/
public class StudenteFile {
	private final static ApplicationContext ctx=new AnnotationConfigApplicationContext(GestioneComuniConfig.class);
	private final static ApplicationContext ctx2=new AnnotationConfigApplicationContext(UniversitaConfig.class);
	
	/*Utilizzato per l'aggiunta di uno studente alla lista presente nel file*/
	public static void scrittura(Studente studente) throws BeansException, NumberFormatException, IOException {
		String f="";
		Set<Studente> studenti=generaStudenti();
		studenti.add(studente);
		File file=new File("src\\main\\resources\\fakedb\\listastudenti.txt");
		for(Studente s:studenti) {
			f+=(s.getMatricola()+";"
			+s.getNome()+";"
			+s.getCognome()+";"
			+s.getDataN().getYear()+"-"+s.getDataN().getMonth()+"-"+s.getDataN().getDay()+";"
			+s.getEmail()+";"
			+s.getCorso().getIdCorso()+";"
			+s.getResidenza().getCitta().getCap()+";"
			+s.getResidenza().getIndirizzo().getVia()+";"
			+s.getResidenza().getIndirizzo().getCivico()+";"+"\n");
		}
		try {
			((Universita)ctx2.getBean("universita")).setStudenti(studenti);
			FileUtils.writeStringToFile(file, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*utilizzato per la modifica od eliminazione di alcuni studenti presenti nella lista*/
	public static void modifica(Set<Studente> students) throws BeansException, NumberFormatException, IOException {
		String f="";
		Set<Studente> studenti=students;
		File file=new File("src\\main\\resources\\fakedb\\listastudenti.txt");
		for(Studente s:studenti) {
			f+=(s.getMatricola()+";"
			+s.getNome()+";"
			+s.getCognome()+";"
			+s.getDataN().getYear()+"-"+s.getDataN().getMonth()+"-"+s.getDataN().getDay()+";"
			+s.getEmail()+";"
			+s.getCorso().getIdCorso()+";"
			+s.getResidenza().getCitta().getCap()+";"
			+s.getResidenza().getIndirizzo().getVia()+";"
			+s.getResidenza().getIndirizzo().getCivico()+";"+"\n");
		}
		try {
			((Universita)ctx2.getBean("universita")).setStudenti(studenti);
			FileUtils.writeStringToFile(file, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*Utilizzato per la lettura dei dati dal file e conversione in un set di studenti*/
	public static Set<Studente> generaStudenti() throws BeansException, NumberFormatException, IOException {
		HashSet<Studente> studenti=new HashSet<>();
		BufferedReader in = null;
		try {
			FileInputStream is = new FileInputStream("src\\main\\resources\\fakedb\\listastudenti.txt");
			in = new BufferedReader(new InputStreamReader(is));
			String line;
			/*Il file viene letto linea per linea*/
			while((line = in.readLine()) != null) {
				/*la linea in lettura viene converita in un array di stringhe
				 * se l'array ha piu di un valore, allora viene riconosciuto come valido*/
				String[] s=line.toString().split(";");
				if(s.length>1) {
					/*Si inizia la raccolta dati dello studente,
					 * prma viene ricercato il corso a cui appartiene*/
					CorsoDiLaurea c=((Universita)ctx2.getBean("universita")).getCorsi().stream()
							.filter(x->x.getIdCorso()==Integer.parseInt(s[5]))
							.collect(Collectors.toList()).get(0);
					
					/*Successivamente viene ricercata la sua citta di residenza*/
					Citta citta=((GestioneCitta)ctx.getBean("generaCitta")).prendiLista()
							.stream().filter(x->x.getCap().equals(s[6])).collect(Collectors.toList()).get(0);
					
					/*viene settata la sua data di nascita tramite
					 * i set Date/Year/Month in qunato unico metodo
					 * insegnatoci per settare le date*/
					String[] dataString=s[3].split("-");
					Date dataS=new Date();
					dataS.setYear(Integer.parseInt(dataString[0]));
					dataS.setMonth(Integer.parseInt(dataString[1]));
					dataS.setDate(Integer.parseInt(dataString[2]));
					
					/*Infine i dati raccolti vengono utilizzati attraverso un builder
					 * per l'istanziamento dello studente*/
					Studente stud=new BuilderStudente(Long.parseLong(s[0]),s[1],s[2])
							.dataNascita(dataS)
							.email(s[4])
							.corso(c)
							.residenza(new Residenza(citta, new Indirizzo(s[7], Integer.parseInt(s[8]))))
							.build();
					studenti.add(stud);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return studenti;
	}
}

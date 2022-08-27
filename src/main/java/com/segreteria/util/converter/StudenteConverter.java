package com.segreteria.util.converter;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.segreteria.config.gestioni.GestioneComuniConfig;
import com.segreteria.model.Studente;
import com.segreteria.model.DTO.StudenteDTO;
import com.segreteria.model.residenza.Indirizzo;
import com.segreteria.model.residenza.Residenza;
import com.segreteria.model.wrapper.GestioneCitta;
import com.segreteria.model.wrapper.GestioneCorsi;
import com.segreteria.util.BuilderStudente;



@Component
public class StudenteConverter implements Converter<StudenteDTO, Studente>{
	
	private static final ApplicationContext ctx2=new AnnotationConfigApplicationContext(GestioneComuniConfig.class);

	@Override
	/*Lo studenteDTO ovvero il dato ricevuto dal form di creazione/modifica viene converito in un istanza Studente*/
	public Studente convert(StudenteDTO source) {
		String[] dataString=source.getDataN().toString().split("-");
		Date data=new Date();
		data.setYear(((Integer.parseInt(dataString[0]))-1900));
		data.setMonth((Integer.parseInt(dataString[1]))-1);
		data.setDate(Integer.parseInt(dataString[2]));
		
		Studente s=null;
		try {
			s=new BuilderStudente(
					source.getMatricola(),
					source.getNome(),
					source.getCognome())
					.dataNascita(data)
					.email(source.getEmail())
					.residenza(new Residenza(
							((GestioneCitta)ctx2.getBean("generaCitta")).prendiLista()
							.stream()
							.filter(x->x.getCap()
									.equals(source.getCapCitta()))
									.collect(Collectors.toList())
									.get(0),
							new Indirizzo(source.getVia(),
										source.getCivico())))
					.corso(new GestioneCorsi().prendiLista()
							.stream()
							.filter(x->x.getIdCorso()==source.getIdCorso())
							.collect(Collectors.toList()).get(0))
					.build();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

}

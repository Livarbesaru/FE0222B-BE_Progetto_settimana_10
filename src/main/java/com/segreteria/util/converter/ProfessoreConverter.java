package com.segreteria.util.converter;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.segreteria.config.UniversitaConfig;
import com.segreteria.config.gestioni.GestioneComuniConfig;
import com.segreteria.model.Professore;
import com.segreteria.model.Universita;
import com.segreteria.model.DTO.ProfessoreDTO;
import com.segreteria.model.residenza.Indirizzo;
import com.segreteria.model.residenza.Residenza;
import com.segreteria.model.wrapper.GestioneCitta;
import com.segreteria.util.BuilderProfessore;

@Component
public class ProfessoreConverter implements Converter<ProfessoreDTO, Professore>{
	
	private static final ApplicationContext ctx2=new AnnotationConfigApplicationContext(GestioneComuniConfig.class);

	@Override
	/*Il professoreDTO ovvero il dato ricevuto dal form di creazione/modifica del professore
	 * viene convertito in un istanza di tipo Professore*/
	public Professore convert(ProfessoreDTO source) {
		String[] dataString=source.getDataN().toString().split("-");
		Date data=new Date();
		data.setYear(((Integer.parseInt(dataString[0]))-1900));
		data.setMonth((Integer.parseInt(dataString[1]))-1);
		data.setDate(Integer.parseInt(dataString[2]));
		
		Professore s=null;
		try {
			s=new BuilderProfessore(
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
					.build();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

}
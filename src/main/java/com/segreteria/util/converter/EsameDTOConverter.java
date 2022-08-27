package com.segreteria.util.converter;

import java.io.IOException;
import java.util.HashSet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.segreteria.config.UniversitaConfig;
import com.segreteria.model.Esame;
import com.segreteria.model.Professore;
import com.segreteria.model.Universita;
import com.segreteria.model.DTO.EsameDTO;
import com.segreteria.model.wrapper.GestioneProfessori;

@Component
public class EsameDTOConverter implements Converter<EsameDTO, Esame> {
	
	static final private ApplicationContext ctx=new AnnotationConfigApplicationContext(UniversitaConfig.class);

		@Override
		/*Il dato EsameDTO ricevuto dal form di creazione/modifica dello stesso, viene
		 * convertito in un istanza di tipo Esame*/
		public Esame convert(EsameDTO source){
			Esame es=new Esame(source.getId(),source.getNome());
			try {
				
				HashSet<Professore> professori=new HashSet<>();
				for(Professore p:((Universita)ctx.getBean("universita")).getProfessori()) {
					if(source.getIdProfessori().stream().anyMatch(x->x.getIdProfessore()==p.getMatricola())) {
						professori.add(p);
					}
				}
				es.setProfessori(professori);
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}
			return es;
		}

}

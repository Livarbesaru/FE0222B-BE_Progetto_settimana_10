package com.segreteria.util.converter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.segreteria.config.UniversitaConfig;
import com.segreteria.model.CorsoDiLaurea;
import com.segreteria.model.Esame;
import com.segreteria.model.Universita;
import com.segreteria.model.DTO.CorsoDiLaureaDTO;
import com.segreteria.model.wrapper.GestioneEsami;
import com.segreteria.model.wrapper.GestioneFacolta;

@Component
public class CorsoDiLaureaDTOConverter implements Converter<CorsoDiLaureaDTO, CorsoDiLaurea> {
	
	static final private ApplicationContext ctx=new AnnotationConfigApplicationContext(UniversitaConfig.class);

	@Override
	/*Il corso di laurea di tipo DTO ricevuto dal form di creazione/modifica del corso
	 * viene convertito in un istanza di tipo CorsoDiLaurea*/
	public CorsoDiLaurea convert(CorsoDiLaureaDTO source){
		CorsoDiLaurea c=new CorsoDiLaurea(
				source.getIdCorso(),
				source.getNumEsami(),
				source.getNome());
		try {
			c.setFacolta(
					((Universita)ctx.getBean("universita")).getFacolta()
					.stream().filter(x->x.getId()==source.getIdFacolta())
					.collect(Collectors.toList()).get(0));
			
			HashSet<Esame> esami=new HashSet<>();
			for(Esame e:((Universita)ctx.getBean("universita")).getEsami()) {
				if(source.getIdEsami().stream().anyMatch(x->x.getIdEsame()==e.getId())) {
					esami.add(e);
				}
			}
			c.setEsami(esami);
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		return c;
	}

}

package com.segreteria.util.converter;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.segreteria.config.UniversitaConfig;
import com.segreteria.model.CorsoDiLaurea;
import com.segreteria.model.Universita;

@Component
public class CorsoDiLaureaConverter implements Converter<String, CorsoDiLaurea> {

	static final private ApplicationContext ctx=new AnnotationConfigApplicationContext(UniversitaConfig.class);
	
	@Override
	/*Il corso di laurea viene ricercato nel fakeDB attraverso l'indice passatogli*/
	public CorsoDiLaurea convert(String source) {
		try {
			return ((Universita)ctx.getBean("universita")).getCorsi().stream()
					.filter(x->x.getIdCorso()==Integer.parseInt(source)).collect(Collectors.toList()).get(0);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (BeansException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}

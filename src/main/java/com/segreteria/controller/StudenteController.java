package com.segreteria.controller;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.segreteria.config.UniversitaConfig;
import com.segreteria.config.gestioni.GestioneComuniConfig;
import com.segreteria.model.Studente;
import com.segreteria.model.Universita;
import com.segreteria.model.DTO.StudenteDTO;
import com.segreteria.model.wrapper.GestioneCitta;
import com.segreteria.model.wrapper.GestioneStudenti;
import com.segreteria.util.converter.StudenteConverter;

@Controller
@RequestMapping("/studente")
public class StudenteController {
	private static final ApplicationContext ctx=new AnnotationConfigApplicationContext(UniversitaConfig.class);
	private static final ApplicationContext ctx2=new AnnotationConfigApplicationContext(GestioneComuniConfig.class);

	/*per l'aggiunta di uno studente si viene reindirizzati al form di creazione*/
	@RequestMapping("/formaggiunta")
	public ModelAndView formStudente() throws NumberFormatException, IOException {
		ModelAndView mv=new ModelAndView("studente\\formAggiuntaStudente");
		mv.addObject("studente", new StudenteDTO());
		mv.addObject("listacomuni", ((GestioneCitta)ctx2.getBean("generaCitta")).prendiLista());
		mv.addObject("corsi",((Universita)ctx.getBean("universita")).getCorsi());
		return mv;
	}
	
	/*compilati i vari campi nel form di creazione si esaminano i campi*/
	@RequestMapping("/aggiunta")
	public Object addStudente(@Valid @ModelAttribute("studente") StudenteDTO studente, BindingResult result) throws BeansException, NumberFormatException, IOException {
		studente.setMatricola(0L);
		if(studente.getNome().replaceAll(" ", "")=="") {
			studente.setNome(null);
		}
		if(studente.getCognome().replaceAll(" ", "")=="") {
			studente.setCognome(null);
		}
		if(studente.getVia().replaceAll(" ", "")=="") {
			studente.setVia(null);
		}
		if(result.hasErrors()) {
			ModelAndView mv= new ModelAndView("studente\\errori");
			System.out.println(studente.getDataN());
			mv.addObject("nome",((studente.getNome()==null) || ((studente.getNome()!=null) && (studente.getNome().length() < 2))));
			mv.addObject("cognome",((studente.getCognome()==null) || ((studente.getCognome()!=null) && (studente.getCognome().length() < 2))));
			mv.addObject("data",((studente.getDataN()==null) || ((studente.getDataN()!=null) && (studente.getDataN().length() < 4))));
			mv.addObject("email",((studente.getEmail()==null) || ((studente.getEmail()!=null) && (studente.getEmail().length() < 5))));
			mv.addObject("citta",((studente.getCapCitta()==null) || ((studente.getCapCitta()!=null) && (studente.getCapCitta().length() <3))));
			mv.addObject("via",((studente.getVia()==null) || ((studente.getVia()!=null) && (studente.getVia().length() <2))));
			return mv;
		}
		
		/*se i campi non presentano alcun errore lo studente viene aggiunto*/
		Set<Studente> studenti=new GestioneStudenti().prendiLista();
		Long id=(long) 0;
		if(studenti.size()>0) {
			for(Studente s:studenti) {
				if(s.getMatricola()>id) {
					id=Long.parseLong((s.getMatricola()+""));
				}
			}
		}
		studente.setMatricola((id+1));
		Studente s=new StudenteConverter().convert(studente);
		new GestioneStudenti().scrittura(s);
		return "redirect:/studente/listastudenti";
	}
	
	@RequestMapping("/listastudenti")
	public ModelAndView listaStudenti() throws NumberFormatException, IOException {
		ModelAndView mv=new ModelAndView("studente\\listaStudenti");
		mv.addObject("studenti", ((Universita)ctx.getBean("universita")).getStudenti());
		return mv;
	}
	
	/*per la modifica dello studente si viene reindirizzati al form di modifica*/
	@RequestMapping("/modifica/{id}")
	public ModelAndView modificaStudente(@PathVariable String id) throws NumberFormatException, IOException {
		ModelAndView mv=new ModelAndView("studente\\formModificaStudente");
		StudenteDTO studente=new StudenteDTO();
		for(Studente s:((Universita)ctx.getBean("universita")).getStudenti()) {
			if(s.getMatricola()==Long.parseLong(id)) {
				studente.setMatricola(Long.parseLong(id));
				studente.setNome(s.getNome());
				studente.setCognome(s.getCognome());
				studente.setDataN(s.getDataN().getYear()+"-"+s.getDataN().getMonth()+"-"+s.getDataN().getDay());
				studente.setEmail(s.getEmail());
				studente.setIdCorso(s.getCorso().getIdCorso());
				studente.setCapCitta(s.getResidenza().getCitta().getCap());
				studente.setVia(s.getResidenza().getIndirizzo().getVia());
				studente.setCivico(s.getResidenza().getIndirizzo().getCivico());
				break;
			}
		}
		studente.setMatricola(Long.parseLong(id));
		mv.addObject("studente",studente);
		mv.addObject("listacomuni",((GestioneCitta)ctx2.getBean("generaCitta")).prendiLista());
		mv.addObject("corsi",((Universita)ctx.getBean("universita")).getCorsi());
		return mv;
	}
	
	/*una volta riempito il form di modifica qui vengono fatti dei controlli*/
	@PostMapping("/modifica/effettiva/{id}")
	public Object modificaStudenteEffettiva(@Valid @ModelAttribute("studente") StudenteDTO studente, BindingResult result,@PathVariable String id) throws NumberFormatException, IOException {
		studente.setMatricola(0L);
		if(studente.getNome().replaceAll(" ", "")=="") {
			studente.setNome(null);
		}
		if(studente.getCognome().replaceAll(" ", "")=="") {
			studente.setCognome(null);
		}
		if(studente.getVia().replaceAll(" ", "")=="") {
			studente.setVia(null);
		}
		if(result.hasErrors()) {
			ModelAndView mv= new ModelAndView("studente\\errori");
			mv.addObject("nome",((studente.getNome()==null) || ((studente.getNome()!=null) && (studente.getNome().length() < 2))));
			mv.addObject("cognome",((studente.getCognome()==null) || ((studente.getCognome()!=null) && (studente.getCognome().length() < 2))));
			mv.addObject("data",((studente.getDataN()==null) || ((studente.getDataN()!=null) && (studente.getDataN().length() < 4))));
			mv.addObject("email",((studente.getEmail()==null) || ((studente.getEmail()!=null) && (studente.getEmail().length() < 5))));
			mv.addObject("citta",((studente.getCapCitta()==null) || ((studente.getCapCitta()!=null) && (studente.getCapCitta().length() <3))));
			mv.addObject("via",((studente.getVia()==null) || ((studente.getVia()!=null) && (studente.getVia().length() <2))));
			return mv;
		}
		/*se nei campi del form di modifica non risultano esserci errori, allora lo studente viene modificato*/
		Set<Studente> studenti=((Universita)ctx.getBean("universita")).getStudenti();
		Studente esa=null;
		if(studenti.size()>0) {
			for(Studente s:studenti) {
				if(s.getMatricola()==Integer.parseInt(id)) {
					esa=s;
					studente.setMatricola(Long.parseLong(id));
					Studente es=new StudenteConverter().convert(studente);
					esa.setDatiStudente(es);
					new GestioneStudenti().modifica(studenti);
				}
			}
		}
		return "redirect:/studente/listastudenti";
	}
	
	/*per l'eliminazione di uno studente si viene reindirizzati ad una pagina di conferma*/
	@RequestMapping("/elimina/{id}")
	public ModelAndView eliminaCorso(@PathVariable String id) throws NumberFormatException, IOException {
		ModelAndView mv=new ModelAndView("studente\\eliminaStudenteConferma");
		Set<Studente> studenti=((Universita)ctx.getBean("universita")).getStudenti();
		Studente s=studenti.stream().filter(x->x.getMatricola()==Long.parseLong(id)).collect(Collectors.toList()).get(0);
		studenti.remove(s);
		mv.addObject("studente",s);
		return mv;
	}
	
	/*una volta confermato, lo studente viene rimosso dal fakedb*/
	@RequestMapping("/elimina/effettiva/{id}")
	public String eliminaCorsoEffettiva(@PathVariable String id) throws NumberFormatException, IOException {
		Set<Studente> studenti=((Universita)ctx.getBean("universita")).getStudenti();
		Studente s=studenti.stream().filter(x->x.getMatricola()==Long.parseLong(id)).collect(Collectors.toList()).get(0);
		studenti.remove(s);
			new GestioneStudenti().modifica(studenti);
			return "redirect:/studente/listastudenti";
	}
}

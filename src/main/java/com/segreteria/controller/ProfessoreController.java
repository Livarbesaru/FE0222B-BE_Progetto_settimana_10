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
import com.segreteria.model.CorsoDiLaurea;
import com.segreteria.model.Esame;
import com.segreteria.model.Facolta;
import com.segreteria.model.Item;
import com.segreteria.model.Professore;
import com.segreteria.model.Studente;
import com.segreteria.model.Universita;
import com.segreteria.model.DTO.ProfessoreDTO;
import com.segreteria.model.DTO.StudenteDTO;
import com.segreteria.model.wrapper.GestioneCitta;
import com.segreteria.model.wrapper.GestioneCorsi;
import com.segreteria.model.wrapper.GestioneEsami;
import com.segreteria.model.wrapper.GestioneFacolta;
import com.segreteria.model.wrapper.GestioneProfessori;
import com.segreteria.model.wrapper.GestioneStudenti;
import com.segreteria.util.converter.ProfessoreConverter;
import com.segreteria.util.converter.StudenteConverter;

@Controller
@RequestMapping("/professore")
public class ProfessoreController {
	
	private static final ApplicationContext ctx=new AnnotationConfigApplicationContext(UniversitaConfig.class);
	private static final ApplicationContext ctx2=new AnnotationConfigApplicationContext(GestioneComuniConfig.class);

	/*Per l'aggiunta di un professore si viene reindirizzati al form di creazione*/
		@RequestMapping("/formaggiunta")
		public ModelAndView formProfessore() throws NumberFormatException, IOException {
			ModelAndView mv=new ModelAndView("professore\\formAggiuntaProfessore");
			mv.addObject("professore", new ProfessoreDTO());
			mv.addObject("listacomuni",new GestioneCitta().prendiLista());
			return mv;
		}
		
		/*una volta compilati i dati questi vengono controllati*/
		@RequestMapping("/aggiunta")
		public Object addProfessori(@Valid @ModelAttribute("professore") ProfessoreDTO professore,BindingResult result) throws BeansException, NumberFormatException, IOException {
			professore.setMatricola(0L);
			if(professore.getNome().replaceAll(" ", "")=="") {
				professore.setNome(null);
			}
			if(professore.getCognome().replaceAll(" ", "")=="") {
				professore.setCognome(null);
			}
			if(professore.getVia().replaceAll(" ", "")=="") {
				professore.setVia(null);
			}
			if(result.hasErrors()) {
				ModelAndView mv= new ModelAndView("professore\\errori");
				mv.addObject("nome",((professore.getNome()==null) || ((professore.getNome()!=null) && (professore.getNome().length() < 2))));
				mv.addObject("cognome",((professore.getCognome()==null) || ((professore.getCognome()!=null) && (professore.getCognome().length() < 2))));
				mv.addObject("data",((professore.getDataN()==null) || ((professore.getDataN()!=null) && (professore.getDataN().length() < 4))));
				mv.addObject("email",((professore.getEmail()==null) || ((professore.getEmail()!=null) && (professore.getEmail().length() < 5))));
				mv.addObject("citta",((professore.getCapCitta()==null) || ((professore.getCapCitta()!=null) && (professore.getCapCitta().length() <3))));
				mv.addObject("via",((professore.getVia()==null) || ((professore.getVia()!=null) && (professore.getVia().length() <2))));
				return mv;
			}
			/*se i dati non presentano errori allora il professore viene aggiunto*/
			Set<Professore> professori=((Universita)ctx.getBean("universita")).getProfessori();
			Long id=(long) 0;
			if(professori.size()>0) {
				for(Professore p:professori) {
					if(p.getMatricola()>id) {
						id=Long.parseLong((p.getMatricola()+""));
					}
				}
			}
			professore.setMatricola((id+1));
			Professore p=new ProfessoreConverter().convert(professore);
			new GestioneProfessori().scrittura(p);
			return "redirect:/professore/listaprofessori";
		}
		
		@RequestMapping("/listaprofessori")
		public ModelAndView listaProfessori() throws NumberFormatException, IOException {
			ModelAndView mv=new ModelAndView("professore\\listaProfessori");
			Set<Professore> professori=((Universita)ctx.getBean("universita")).getProfessori();
			mv.addObject("professori",professori);
			return mv;
		}
		
		/*per la modifica di un professore si viene reindirizzati al form di modifica*/
		@RequestMapping("/modifica/{id}")
		public ModelAndView modificaProfessore(@PathVariable String id) throws NumberFormatException, IOException {
			ModelAndView mv=new ModelAndView("professore\\formModificaProfessore");
			ProfessoreDTO professore=new ProfessoreDTO();
			for(Professore p:((Universita)ctx.getBean("universita")).getProfessori()) {
				if(p.getMatricola()==Long.parseLong(id)) {
					professore.setMatricola(Long.parseLong(id));
					professore.setNome(p.getNome());
					professore.setCognome(p.getCognome());
					professore.setDataN(p.getDataN().getYear()+"-"+p.getDataN().getMonth()+"-"+p.getDataN().getDay());
					professore.setEmail(p.getEmail());
					professore.setCapCitta(p.getResidenza().getCitta().getCap());
					professore.setVia(p.getResidenza().getIndirizzo().getVia());
					professore.setCivico(p.getResidenza().getIndirizzo().getCivico());
					break;
				}
			}
			professore.setMatricola(Long.parseLong(id));
			mv.addObject("professore",professore);
			mv.addObject("listacomuni",((GestioneCitta)ctx2.getBean("generaCitta")).prendiLista());
			return mv;
		}
		
		/*Una volta riempiti i campi nel form di modifica questi vengono controllati
		 * se non presentano errori il professore viene modificato*/
		@PostMapping("/modifica/effettiva/{id}")
		public Object modificaProfessoreEffettiva(@Valid @ModelAttribute("professore") ProfessoreDTO professore,BindingResult result,@PathVariable String id) throws NumberFormatException, IOException {
			professore.setMatricola(0L);
			if(professore.getNome().replaceAll(" ", "")=="") {
				professore.setNome(null);
			}
			if(professore.getCognome().replaceAll(" ", "")=="") {
				professore.setCognome(null);
			}
			if(professore.getVia().replaceAll(" ", "")=="") {
				professore.setVia(null);
			}
			if(result.hasErrors()) {
				ModelAndView mv= new ModelAndView("professore\\errori");
				mv.addObject("nome",((professore.getNome()==null) || ((professore.getNome()!=null) && (professore.getNome().length() < 2))));
				mv.addObject("cognome",((professore.getCognome()==null) || ((professore.getCognome()!=null) && (professore.getCognome().length() < 2))));
				mv.addObject("data",((professore.getDataN()==null) || ((professore.getDataN()!=null) && (professore.getDataN().length() < 4))));
				mv.addObject("email",((professore.getEmail()==null) || ((professore.getEmail()!=null) && (professore.getEmail().length() < 5))));
				mv.addObject("citta",((professore.getCapCitta()==null) || ((professore.getCapCitta()!=null) && (professore.getCapCitta().length() <3))));
				mv.addObject("via",((professore.getVia()==null) || ((professore.getVia()!=null) && (professore.getVia().length() <2))));
				return mv;
			}
			Set<Professore> professori=((Universita)ctx.getBean("universita")).getProfessori();
			Professore esa=null;
			if(professori.size()>0) {
				for(Professore p:professori) {
					if(p.getMatricola()==Integer.parseInt(id)) {
						esa=p;
						professore.setMatricola(Long.parseLong(id));
						Professore pf=new ProfessoreConverter().convert(professore);
						esa.setDatiProfessore(pf);
						new GestioneProfessori().modifica(professori);
					}
				}
			}
			return "redirect:/professore/listaprofessori";
		}
		
		/*per l'eliminazione di un professore si viene reindirizzati ad una pagina di conferma
		 * dove scegliere quale professore prendera il suo posto nei vari esami*/
		@RequestMapping("/elimina/{id}")
		public ModelAndView eliminaProfessore(@PathVariable String id) throws NumberFormatException, IOException {
			ModelAndView mv=new ModelAndView("professore\\eliminaProfessoreConferma");
			Set<Professore> professori=((Universita)ctx.getBean("universita")).getProfessori();
			Professore p=professori.stream().filter(x->x.getMatricola()==Long.parseLong(id)).collect(Collectors.toList()).get(0);
			professori.remove(p);
			mv.addObject("prof",p);
			mv.addObject("professori",professori);
			mv.addObject("professoreScelto",new Item());
			return mv;
		}
		
		/*confermata l'eliminazione questa viene effettuata con un controllo per vedere
		 * se l'universita ha un numero di professori maggiori almeno ad un uno*/
		@RequestMapping("/elimina/effettiva/{id}")
		public Object eliminaProfessoreEffettiva(@PathVariable String id,@ModelAttribute("professoreScelto") Item prof) throws NumberFormatException, IOException {
			ModelAndView mv=new ModelAndView("professore\\eliminaerrore");
			Set<Professore> professori=((Universita)ctx.getBean("universita")).getProfessori();
			Set<Esame> esami=((Universita)ctx.getBean("universita")).getEsami();
			Professore profDaEliminare=professori.stream().filter(x->x.getMatricola()==Long.parseLong(id)).collect(Collectors.toList()).get(0);
			Professore profscelto=professori.stream().filter(x->x.getMatricola()==Long.parseLong((prof.getId()+""))).collect(Collectors.toList()).get(0);
			if(professori.size()>1) {
				for(Esame e:esami) {
					for(Professore p:e.getProfessori()) {
						if(p.getMatricola()==profDaEliminare.getMatricola()) {
							p.setDatiProfessore(profscelto);
							p.setMatricola(profscelto.getMatricola());
						}
					}
				}
				professori.remove(profDaEliminare);
				new GestioneEsami().modifica(esami);
				new GestioneProfessori().modifica(professori);
				return "redirect:/professore/listaprofessori";
			}
			else {
				mv.addObject("errore",(professori.size()<=1));
				return mv;
			}
		}
}

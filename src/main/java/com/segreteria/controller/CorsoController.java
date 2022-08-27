package com.segreteria.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import com.segreteria.model.CorsoDiLaurea;
import com.segreteria.model.Esame;
import com.segreteria.model.Facolta;
import com.segreteria.model.Item;
import com.segreteria.model.Studente;
import com.segreteria.model.Universita;
import com.segreteria.model.DTO.CorsoDiLaureaDTO;
import com.segreteria.model.DTO.LongEsamiDTO;
import com.segreteria.model.wrapper.GestioneCorsi;
import com.segreteria.model.wrapper.GestioneFacolta;
import com.segreteria.model.wrapper.GestioneStudenti;
import com.segreteria.util.converter.CorsoDiLaureaDTOConverter;

@Controller
@RequestMapping("/corso")
public class CorsoController {
	static final private ApplicationContext ctx=new AnnotationConfigApplicationContext(UniversitaConfig.class);

	/*Si viene reindirizzati ad una pagina dove si sceglie la quantita di esami presneti nel corso*/
		@RequestMapping("/aggiuntacorso")
		public ModelAndView numeroEsami() throws NumberFormatException, BeansException, IOException {
			ModelAndView mv=new ModelAndView("corso\\sceltaNumEsami");
			List<Item> items=new ArrayList<>();
			for(int i=10;i<=(((Universita)ctx.getBean("universita")).getEsami().size());i++) {
				if(i>=10 && i<34) {
					items.add(new Item(i));
				}
				else {
					break;
				}
			}
			mv.addObject("items",items);
			mv.addObject("numEsami",new Item());
			return mv;
		}
	
		/*Dopo la scelta di quanti esami il corso debba avere,
		 * si viene reindirizzati al riempimento dei dati del corso*/
		@RequestMapping("/formaggiunta")
		public ModelAndView formCorso(@ModelAttribute("numEsami") Item item) throws NumberFormatException, IOException {
			ModelAndView mv=new ModelAndView("corso\\formAggiuntaCorso");
			CorsoDiLaureaDTO corso=new CorsoDiLaureaDTO();
			corso.setIdCorso(0);
			for(int i=0;i<item.getId();i++) {
				corso.aggiungiEsame(Long.parseLong((i+"")));
			}
			/*vengono caricati tutti i possibili esami, corsi e facolta presenti nell'universià*/
			mv.addObject("esami",((Universita)ctx.getBean("universita")).getEsami());
			mv.addObject("corso",corso);
			mv.addObject("facolta",((Universita)ctx.getBean("universita")).getFacolta());
			return mv;
		}
		
		/*Dopo il riempimento dei dati del corso questi vengono lavorati*/
		@RequestMapping("/aggiunta")
		public Object addCorso(@Valid @ModelAttribute("corso") CorsoDiLaureaDTO corso,BindingResult result) throws NumberFormatException, IOException {
			boolean controlloEsami=false;
			
			/*Vengono controllati se i dati sono validi
			 * se alcuni campi risultano non validi allora si viene reindirizzati ad una pagina
			 * di errore*/
			if(corso.getNome().replaceAll(" ", "")=="") {
				corso.setNome(null);
			}
			if(corso.getNome()!=null) {
				for(LongEsamiDTO e:corso.getIdEsami()) {
					if(corso.getIdEsami().stream().filter(x->x.getIdEsame()==e.getIdEsame()).collect(Collectors.toList()).size()>=2) {
						if(controlloEsami==false) {
							controlloEsami=true;
						}
					}
				}
			}
			
			if(result.hasErrors() || controlloEsami || corso.getNome()==null) {
				ModelAndView mv = new ModelAndView("corso\\errori");
				mv.addObject("corsoEsami",(corso.getIdEsami().size()<10) || controlloEsami);
				mv.addObject("corsoNome",(corso.getNome()==null));
				return mv;
			}
			
			/*Se passano i controlli si aggiunge il corso*/
			Set<CorsoDiLaurea> corsi=((Universita)ctx.getBean("universita")).getCorsi();
			int id=0;
			if(corsi.size()>0) {
				for(CorsoDiLaurea c:corsi) {
					if(c.getIdCorso()>id) {
						id=c.getIdCorso();
					}
				}
			}
			corso.setIdCorso((id+1));
			/*Il corsoDiLaureaDTO viene converitito in un CorsoDiLaurea*/
			CorsoDiLaurea c= new CorsoDiLaureaDTOConverter().convert(corso);
			new GestioneCorsi().scrittura(c);
			return "redirect:/corso/listacorsi";
		}
		
		/*Per la modifica del corso vengono scelti prima quanti esami debba avere*/
		@RequestMapping("/modifica/numesami/{id}")
		public ModelAndView numeroEsamiModifica(@PathVariable String id) throws NumberFormatException, BeansException, IOException {
			ModelAndView mv=new ModelAndView("corso\\sceltaNumEsamiModifica");
			List<Item> items=new ArrayList<>();
			int esamiSize=(((Universita)ctx.getBean("universita")).getEsami().size());
			for(int i=10;i<=esamiSize;i++) {
				if(i>=10 && i<34) {
					items.add(new Item(i));
				}
				else {
					break;
				}
			}
			mv.addObject("items",items);
			mv.addObject("numEsami",new Item());
			mv.addObject("idcorso",new Item(Integer.parseInt(id)));
			return mv;
		}
	
		/*Una volta scelto quanti esami debba avere si viene reindirizzati al form di modifica*/
		@RequestMapping("/modifica/form/{id}")
		public ModelAndView formCorsoModifca(@ModelAttribute("numEsami") Item item,@PathVariable String id) throws NumberFormatException, IOException {
			ModelAndView mv=new ModelAndView("corso\\formModificaCorso");
			CorsoDiLaureaDTO corso=new CorsoDiLaureaDTO();
			CorsoDiLaurea corsoBase=((Universita)ctx.getBean("universita")).getCorsi()
					.stream().filter(x->x.getIdCorso()==Integer.parseInt(id)).collect(Collectors.toList()).get(0);
			corso.setIdCorso(Integer.parseInt(id));
			corso.setIdFacolta(corsoBase.getFacolta().getId());
			corso.setNome(corsoBase.getNome());
			corso.setIdEsami(new ArrayList<LongEsamiDTO>());
			for(int i=0;i<item.getId();i++){
				corso.getIdEsami().add(new LongEsamiDTO(Long.parseLong((i+""))));
			}
			/*vengono caricati tutti i possibili esami, corsi e facolta presenti nell'universià*/
			mv.addObject("esami",((Universita)ctx.getBean("universita")).getEsami());
			mv.addObject("corso",corso);
			mv.addObject("facolta",((Universita)ctx.getBean("universita")).getFacolta());
			return mv;
		}
		
		/*Una volta compiuto il riempimento dei campi i dati vengono lavorati*/
		@PostMapping("/modifica/effettiva/{id}")
		public Object modificaFacolta(@ModelAttribute("corso") CorsoDiLaureaDTO corso,BindingResult result,@PathVariable String id) throws NumberFormatException, IOException {
			boolean controlloEsami=false;
			
			/*Si studia se i dati inseriti rispettino delle regole
			 * altrimenti si viene rendirizzati ad una pagina di errore*/
			if(corso.getNome().replaceAll(" ", "")=="") {
				corso.setNome(null);
			}
			if(corso.getNome()!=null) {
				for(LongEsamiDTO e:corso.getIdEsami()) {
					if(corso.getIdEsami().stream().filter(x->x.getIdEsame()==e.getIdEsame()).collect(Collectors.toList()).size()>=2) {
						if(controlloEsami==false) {
							controlloEsami=true;
						}
					}
				}
			}
			
			if(result.hasErrors() || controlloEsami || corso.getNome()==null) {
				boolean verifica=((corso.getIdEsami().size()<10) || controlloEsami);
				ModelAndView mv = new ModelAndView("corso\\errori");
				mv.addObject("corsoEsami",verifica);
				mv.addObject("corsoNome",(corso.getNome()==null));
				return mv;
			}
			/*Se si passano i controlli il corso viene modificato ed il fakedb viene aggiornato*/
			Set<CorsoDiLaurea> corsi=new GestioneCorsi().prendiLista();
			CorsoDiLaurea cors=null;
			if(corsi.size()>0) {
				for(CorsoDiLaurea c:corsi) {
					if(c.getIdCorso()==Integer.parseInt(id)) {
						cors=c;
					}
				}
				corso.setIdCorso(Integer.parseInt(id));
				corso.setNumEsami(corso.getIdEsami().size());
				CorsoDiLaurea corsoD=new CorsoDiLaureaDTOConverter().convert(corso);
				cors.setDatiCorso(corsoD);
				new GestioneCorsi().modifica(corsi);
			}
			return "redirect:/corso/listacorsi";
		}
		
		@RequestMapping("/listacorsi")
		public ModelAndView listaCorsi() throws NumberFormatException, IOException {
			ModelAndView mv=new ModelAndView("corso\\listaCorsi");
			Set<CorsoDiLaurea> corsi=((Universita)ctx.getBean("universita")).getCorsi();
			mv.addObject("corsi",corsi);
			return mv;
		}
		
		/*per l'eliminazione di un corso si viene reindirizzati ad una pagina di conferma*/
		@RequestMapping("/elimina/{id}")
		public ModelAndView eliminaCorso(@PathVariable String id) throws NumberFormatException, IOException {
			ModelAndView mv=new ModelAndView("corso\\eliminaCorsoConferma");
			Set<CorsoDiLaurea> corsi=((Universita)ctx.getBean("universita")).getCorsi();
			CorsoDiLaurea c=corsi.stream().filter(x->x.getIdCorso()==Integer.parseInt(id)).collect(Collectors.toList()).get(0);
			corsi.remove(c);
			mv.addObject("corso",c);
			mv.addObject("corsi",corsi);
			mv.addObject("corsoScelto",new Item());
			return mv;
		}
		
		/*una volta confermata l'intenzione di volere eliminare il corso esso viene rimosso dalla lista nel file*/
		@RequestMapping("/elimina/effettiva/{id}")
		public Object eliminaCorsoEffettiva(@PathVariable String id,@ModelAttribute("corsoScelto") Item corso) throws NumberFormatException, IOException {
			ModelAndView mv=new ModelAndView("corso\\eliminaerrore");
			Set<CorsoDiLaurea> corsi=((Universita)ctx.getBean("universita")).getCorsi();
			Set<Studente> studenti=((Universita)ctx.getBean("universita")).getStudenti();
			CorsoDiLaurea corsoDaEliminare=corsi.stream().filter(x->x.getIdCorso()==Integer.parseInt(id)).collect(Collectors.toList()).get(0);
			CorsoDiLaurea corsoScelto=corsi.stream().filter(x->x.getIdCorso()==corso.getId()).collect(Collectors.toList()).get(0);
			if(corsi.size()>1) {
				/*E obbligatorio che almeno un corso sia presente nell'universita, altrimenti 
				 * si viene rimandati ad una pagina di errore*/
				for(Studente s:studenti) {
					if(s.getCorso().getIdCorso()==corsoDaEliminare.getIdCorso()) {
						s.setCorso(corsoScelto);
					}
				}
				corsi.remove(corsoDaEliminare);
				new GestioneStudenti().modifica(studenti);
				new GestioneCorsi().modifica(corsi);
				return "redirect:/corso/listacorsi";
			}
			else {
				mv.addObject("errore",(corsi.size()<=1));
				return mv;
			}
		}
}

package com.segreteria.controller;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.segreteria.config.UniversitaConfig;
import com.segreteria.model.CorsoDiLaurea;
import com.segreteria.model.Facolta;
import com.segreteria.model.Item;
import com.segreteria.model.Universita;
import com.segreteria.model.wrapper.GestioneCorsi;
import com.segreteria.model.wrapper.GestioneFacolta;

@Controller
@RequestMapping("/facolta")
public class FacoltaController {
	
	private static final ApplicationContext ctx=new AnnotationConfigApplicationContext(UniversitaConfig.class);

	/*Si viene reindirizzati al form di creazione per le facolta*/
	@GetMapping("/formaggiunta")
		public ModelAndView formFacolta() {
			ModelAndView mv=new ModelAndView("facolta\\formAggiuntaFacolta");
			mv.addObject("facolta", new Facolta());
			return mv;
	}
		
	/*Una volta riempiti i campi questi vengono controllati*/
	@PostMapping("/aggiunta")
		public Object addFacolta(@Valid @ModelAttribute("facolta") Facolta facolta,BindingResult result) throws NumberFormatException, IOException {
		if(facolta.getNomeFacolta().replaceAll(" ", "")=="") {
			facolta.setNomeFacolta(null);
		}	
		if(result.hasErrors() || facolta.getNomeFacolta()==null) {
			ModelAndView mv=new ModelAndView("facolta\\errori");
			mv.addObject("nome", facolta.getNomeFacolta()==null);
			return mv;
		}
		/*Se nei controlli non risultano errori allora si procede all'aggiunta della facolta*/
		Set<Facolta> facoltas=new GestioneFacolta().prendiLista();
			int id=0;
			if(facoltas.size()>0) {
				for(Facolta f:facoltas) {
					if(f.getId()>id) {
						id=f.getId();
					}
				}
			}
			facolta.setId((id+1));
			new GestioneFacolta().scrittura(facolta);
			return "redirect:/facolta/listafacolta";
	}
	
	/*Per la modifica di una facolta si viene reindirizzati al form di modifica*/
	@GetMapping("/modifica/{id}")
	public ModelAndView modificaFacolta(@PathVariable String id) throws NumberFormatException, IOException {
		Set<Facolta> facoltas=new GestioneFacolta().prendiLista();
		ModelAndView mv=new ModelAndView("facolta\\formModificaFacolta");
		Facolta f=facoltas.stream().filter(x->x.getId()==(Integer.parseInt(id))).collect(Collectors.toList()).get(0);
		mv.addObject("facolta",f);
		return mv;
	}
	
	/*Una volta compilati i campi questi vengono controllati*/
	@PostMapping("/modifica/effettiva/{id}")
	public Object modificaFacolta(@ModelAttribute("facolta") Facolta facolta,BindingResult result,@PathVariable String id) throws NumberFormatException, IOException {
		if(facolta.getNomeFacolta().replaceAll(" ", "")=="") {
			facolta.setNomeFacolta(null);
		}	
		if(result.hasErrors() || facolta.getNomeFacolta()==null) {
			ModelAndView mv=new ModelAndView("facolta\\errori");
			mv.addObject("nome", facolta.getNomeFacolta()==null);
			return mv;
		}
		/*se non presentano errori la facolta viene modificata*/
		Set<Facolta> facoltas=new GestioneFacolta().prendiLista();
		Facolta fac=null;
		if(facoltas.size()>0) {
			for(Facolta f:facoltas) {
				if(f.getId()==Integer.parseInt(id)) {
					fac=f;
				}
			}
			fac.setDatiFacolta(facolta);
			new GestioneFacolta().modifica(facoltas);
		}
		return "redirect:/facolta/listafacolta";
	}
	
	@RequestMapping("/listafacolta")
	public ModelAndView listaFacolta() throws NumberFormatException, IOException {
		ModelAndView mv=new ModelAndView("facolta\\listaFacolta");
		Set<Facolta> facolta=((Universita)ctx.getBean("universita")).getFacolta();
		mv.addObject("facolta",facolta);
		return mv;
	}
	
	/*per l'eliminazione di una facolta si viene rimandati ad una pagina di conferma
	 * dove si deve sceglie una facolta su cui spostare i corsi associati alla facolta in fase di eliminazione*/
	@RequestMapping("/elimina/{id}")
	public ModelAndView eliminaFacolta(@PathVariable String id) throws NumberFormatException, IOException {
		ModelAndView mv=new ModelAndView("facolta\\eliminaFacoltaConferma");
		Set<Facolta> facolta=((Universita)ctx.getBean("universita")).getFacolta();
		Facolta f=facolta.stream().filter(x->x.getId()==Integer.parseInt(id)).collect(Collectors.toList()).get(0);
		facolta.remove(f);
		mv.addObject("fac",f);
		mv.addObject("facolta",facolta);
		mv.addObject("facoltaScelta",new Item());
		return mv;
	}
	
	/*una volta confermato se il numero di facolta presenti nell'università è maggiore
	 * ad uno, allora la facolta viene cancellata*/
	@RequestMapping("/elimina/effettiva/{id}")
	public Object eliminaFacoltaEffettiva(@PathVariable String id,@ModelAttribute("facoltaScelta") Item facolta) throws NumberFormatException, IOException {
		ModelAndView mv=new ModelAndView("facolta\\eliminaerrore");
		Set<Facolta> facoltas=((Universita)ctx.getBean("universita")).getFacolta();
		Set<CorsoDiLaurea> corsi=((Universita)ctx.getBean("universita")).getCorsi();
		Facolta facoltaDaEliminare=facoltas.stream().filter(x->x.getId()==Integer.parseInt(id)).collect(Collectors.toList()).get(0);
		Facolta facoltascelta=facoltas.stream().filter(x->x.getId()==facolta.getId()).collect(Collectors.toList()).get(0);
		if(facoltas.size()>1) {
			for(CorsoDiLaurea c:corsi) {
				if(c.getFacolta().getId()==facoltaDaEliminare.getId()) {
					c.setFacolta(facoltascelta);
				}
			}
			facoltas.remove(facoltaDaEliminare);
			new GestioneFacolta().modifica(facoltas);
			new GestioneCorsi().modifica(corsi);
			return "redirect:/facolta/listafacolta";
		}
		else {
			mv.addObject("errore",(facoltas.size()<=1));
			return mv;
		}
	}

}

package com.segreteria.controller;

import java.io.IOException;
import java.util.HashSet;
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
import com.segreteria.model.Professore;
import com.segreteria.model.Universita;
import com.segreteria.model.DTO.EsameDTO;
import com.segreteria.model.DTO.LongProfessoriDTO;
import com.segreteria.model.wrapper.GestioneCorsi;
import com.segreteria.model.wrapper.GestioneEsami;
import com.segreteria.model.wrapper.GestioneProfessori;
import com.segreteria.util.converter.EsameDTOConverter;

@Controller
@RequestMapping("/esame")
public class EsameController {
	private static final ApplicationContext ctx = new AnnotationConfigApplicationContext(UniversitaConfig.class);

	/*per l'aggiunta dell'esame si viene reindirizzati al form di creazione dell'esame*/
	@RequestMapping("/formaggiunta")
	public ModelAndView formEsame() throws BeansException, NumberFormatException, IOException {
		ModelAndView mv = new ModelAndView("esame\\formAggiuntaEsame");
		EsameDTO esame = new EsameDTO();
		for (int i = 0; i < 2; i++) {
			esame.getIdProfessori().add(new LongProfessoriDTO(Long.parseLong((i + ""))));
		}
		/*vengono caricati i professori presenti nell'universita cosi da permettere la scelta*/
		mv.addObject("esame", esame);
		mv.addObject("professori", new GestioneProfessori().prendiLista());
		return mv;
	}

	/*una volta riempiti i campi questi vengono controllati*/
	@RequestMapping("/aggiunta")
	public Object addEsame(@Valid @ModelAttribute("esame") EsameDTO esame, BindingResult result)
			throws NumberFormatException, IOException {
		if (esame.getNome().replaceAll(" ", "") == "") {
			esame.setNome(null);
		}
		if (result.hasErrors() || esame.getIdProfessori().size() == 0
				|| (esame.getIdProfessori().size() > 1 && esame.getIdProfessori().get(0).getIdProfessore() == null
						&& esame.getIdProfessori().get(1).getIdProfessore() == null)) {
			ModelAndView mv = new ModelAndView("esame\\errori");
			mv.addObject("esameNome", esame.getNome() == null);
			mv.addObject("esame0",
					(esame.getIdProfessori().size() == 0) || (esame.getIdProfessori().size() > 1
							&& esame.getIdProfessori().get(0).getIdProfessore() == null
							&& esame.getIdProfessori().get(1).getIdProfessore() == null));
			mv.addObject("esameLunghezza", (esame.getIdProfessori().size() == 2
					&& (esame.getIdProfessori().get(0) == esame.getIdProfessori().get(1))));
			return mv;
		}
		/*se i campi non presentano errori allora l'esame viene aggiunto al fakedb assegnandogli un indice*/
		Set<Esame> esami = new GestioneEsami().prendiLista();
		Long id = (long) 0;
		if (esami.size() > 1) {
			for (Esame e : esami) {
				if (e.getId() > id) {
					id = Long.parseLong((e.getId() + ""));
				}
			}
		}
		esame.setId((id + 1));
		Esame e = new EsameDTOConverter().convert(esame);
		new GestioneEsami().scrittura(e);
		return "redirect:/esame/listaesami";
	}

	/*per la modifica del corso si viene reindirizzati al form di modifica*/
	@RequestMapping("/modifica/{id}")
	public ModelAndView modificaEsame(@PathVariable String id) throws NumberFormatException, IOException {
		ModelAndView mv = new ModelAndView("esame\\formModificaEsame");
		EsameDTO esame = new EsameDTO();
		for (Esame e : ((Universita) ctx.getBean("universita")).getEsami()) {
			if (e.getId() == Long.parseLong(id)) {
				esame.setNome(e.getNome());
				for (Professore p : e.getProfessori()) {
					esame.getIdProfessori().add(new LongProfessoriDTO(p.getMatricola()));
				}
				break;
			}
		}
		if (esame.getIdProfessori().size() < 2) {
			esame.getIdProfessori().add(new LongProfessoriDTO());
		}
		esame.setId(Long.parseLong(id));
		mv.addObject("esame", esame);
		mv.addObject("professori", new GestioneProfessori().prendiLista());
		return mv;
	}
	
	/*una volta riempiti i campi del form di modifica vengono effettuati dei controlli*/
	@PostMapping("/modifica/effettiva/{id}")
	public Object modificaFacolta(@Valid @ModelAttribute("esame") EsameDTO esame, BindingResult result,
			@PathVariable String id) throws NumberFormatException, IOException {
		if (esame.getNome().replaceAll(" ", "") == "") {
			esame.setNome(null);
		}
		if (result.hasErrors() || esame.getIdProfessori().size() == 0
				|| (esame.getIdProfessori().size() > 1 && esame.getIdProfessori().get(0).getIdProfessore() == null
						&& esame.getIdProfessori().get(1).getIdProfessore() == null)) {
			ModelAndView mv = new ModelAndView("esame\\errori");
			mv.addObject("esameNome", esame.getNome() == null);
			mv.addObject("esame0",
					(esame.getIdProfessori().size() == 0) || (esame.getIdProfessori().size() > 1
							&& esame.getIdProfessori().get(0).getIdProfessore() == null
							&& esame.getIdProfessori().get(1).getIdProfessore() == null));
			mv.addObject("esameLunghezza", (esame.getIdProfessori().size() == 2
					&& (esame.getIdProfessori().get(0) == esame.getIdProfessori().get(1))));
			return mv;
		}
		/*se i controlli non trovano errori allora l'esame viene modificato*/
		Set<Esame> esami = new GestioneEsami().prendiLista();
		Esame esa = null;
		if (esami.size() > 0) {
			for (Esame e : esami) {
				if (e.getId() == Integer.parseInt(id)) {
					esa = e;
					Esame es = new EsameDTOConverter().convert(esame);
					esa.setDatiEsame(es);
					new GestioneEsami().modifica(esami);
				}
			}
		}
		return "redirect:/esame/listaesami";
	}

	/*per l'eliminazione dell'esame si viene reindirizzati ad una pagina di conferma*/
	@RequestMapping("/elimina/{id}")
	public ModelAndView eliminaEsame(@PathVariable String id) throws NumberFormatException, IOException {
		ModelAndView mv = new ModelAndView("esame\\eliminaEsameConferma");
		Esame e = ((Universita) ctx.getBean("universita")).getEsami().stream()
				.filter(x -> x.getId() == Long.parseLong(id)).collect(Collectors.toList()).get(0);
		mv.addObject("esame", e);
		return mv;
	}

	/*Una volta confermata l'eliminazione vengono effettuati dei controlli*/
	@RequestMapping("/elimina/effettiva/{id}")
	public Object eliminaEffettivaEsame(@PathVariable String id)
			throws NumberFormatException, BeansException, IOException {
		Set<CorsoDiLaurea> corsi = ((Universita) ctx.getBean("universita")).getCorsi();
		Set<CorsoDiLaurea> corsi_nonModificare = new HashSet<CorsoDiLaurea>();
		Set<Esame> esami = ((Universita) ctx.getBean("universita")).getEsami();
		Esame esame = esami.stream().filter(x -> x.getId() == Long.parseLong(id)).collect(Collectors.toList()).get(0);

		/*Vengono letti quali corsi presentano l' esame che si desidera eliminare
		 * e se almeno uno di questi ha 10 esami, allora si viene reindirizzati ad una pagina di errore*/
		for (CorsoDiLaurea c : corsi) {
			if (c.getEsami().stream().anyMatch(x -> x.getId() == esame.getId()) && c.getEsami().size() <= 10) {
				corsi_nonModificare.add(c);
			}
		}

		if (corsi_nonModificare.size() > 0 || esami.size() <= 10) {
			ModelAndView mv = new ModelAndView("esame\\eliminaErrore");
			mv.addObject("corsi", corsi_nonModificare);
			mv.addObject("numEsamiErrore", (esami.size() <= 10));
			return mv;
		} else {
			for (CorsoDiLaurea c : corsi) {
				for (Esame e : c.getEsami()) {
					if (e.getId() == esame.getId() && c.getEsami().size() > 10) {
						c.getEsami().remove(e);
					}
				}
			}
			for (CorsoDiLaurea c : corsi) {
				c.setNumEsami(c.getEsami().size());
			}
			esami.remove(esame);
			new GestioneEsami().modifica(esami);
			new GestioneCorsi().modifica(corsi);
			return "redirect:/esame/listaesami";
		}
	}

	@RequestMapping("/listaesami")
	public ModelAndView listaEsami() throws NumberFormatException, IOException {
		ModelAndView mv = new ModelAndView("esame\\listaEsami");
		Set<Esame> esami = ((Universita) ctx.getBean("universita")).getEsami();
		mv.addObject("esami", esami);
		return mv;
	}
}

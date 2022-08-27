package com.segreteria.model.DTO;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CorsoDiLaureaDTO {
	private Integer idCorso=0;
	private int numEsami;
	
	@Size(min=10)
	private List<LongEsamiDTO> idEsami=new ArrayList<>();
	
	@Size(min=2)
	private String nome;
	
	private int idFacolta;
	
	public void aggiungiEsame(Long id) {
		idEsami.add(new LongEsamiDTO(id));
	}
}

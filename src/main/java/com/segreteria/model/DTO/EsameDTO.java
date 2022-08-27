package com.segreteria.model.DTO;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EsameDTO {
	private Long id;
	
	@Size(min=5)
	private String nome;
	
	@Size(min=1)
	private List<LongProfessoriDTO> idProfessori=new ArrayList<>();
}

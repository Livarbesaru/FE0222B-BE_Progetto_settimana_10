package com.segreteria.model;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Facolta {
	
	private int id;
	
	@Size(min=2)
	private String nomeFacolta;
	
	public void setId(int id) {
		this.id=id;
	}
	
	public void setDatiFacolta(Facolta facolta) {
		this.nomeFacolta=facolta.nomeFacolta;
	}
	
}

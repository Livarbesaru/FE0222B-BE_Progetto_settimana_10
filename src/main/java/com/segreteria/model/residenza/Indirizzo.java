package com.segreteria.model.residenza;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@NonNull
public class Indirizzo {
	private String via;
	private int civico;
}

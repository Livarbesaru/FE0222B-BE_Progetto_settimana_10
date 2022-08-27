package com.segreteria.model.residenza;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@NonNull
public class Residenza {
	private Citta citta;
	private Indirizzo indirizzo;
}

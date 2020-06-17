package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

public enum Colore {
	BLU,
	GREEN,
	YELLOW,
	RED;
	
	public static List<String> allColors() {
		List<String> colori = new ArrayList<String>();
		for(Colore color : Colore.values()) {
			colori.add(color.name().toLowerCase());
		}
		return colori;
	}
}

package com.busenzo.domein;

import java.util.ArrayList;

public class Lijn {
	private int id;
	private int nummer;
	private Boolean richting;
	private String beschrijving;
	public ArrayList<Rit> ritten = new ArrayList<>();
	public ArrayList<Halte> haltes = new ArrayList<>();

	public Lijn(int nummer, boolean richting, String beschrijving) {
		throw new UnsupportedOperationException();
	}

	public void addHalte(Halte halte) {
		throw new UnsupportedOperationException();
	}

	public void addHaltes(Halte[] haltes) {
		throw new UnsupportedOperationException();
	}
}
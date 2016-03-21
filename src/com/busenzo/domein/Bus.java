package com.busenzo.domein;

import java.util.ArrayList;

public class Bus {
    
	private int nummer;
	private int lon;
	private int lat;
	public Rit rit;
	public ArrayList<Melding> meldingen = new ArrayList<>();

	public Bus(int nummer) {
		throw new UnsupportedOperationException();
                //TODO
	}

	public void updateLocatie(int lon, int lat) {
		throw new UnsupportedOperationException();
	}
}
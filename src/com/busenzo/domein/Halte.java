package com.busenzo.domein;

import com.busenzo.administratie.Administratie;
import java.util.ArrayList;
import java.util.List;

public class Halte {
	private String naam;
	private int lon;
	private int lat;
	public Administratie unnamed_Administratie_;
	public ArrayList<Lijn> unnamed_Lijn_ = new ArrayList<Lijn>();

        /**
         * Maak een nieuwe halte aan, er mag nog geen halte bestaan met dezelfde naam en lat/lon
         * @param naam: Naam van de halte, mag geen lege string zijn
         * @param lon: Longditude van de halte, mag niet null zijn
         * @param lat: Ladditude van de halte, mag niet null zijn
         */
	public Halte(Object naam, Object lon, Object lat) {
		throw new UnsupportedOperationException();
	}

        /**
         * Voeg een lijn toe die bij deze halte stopt. Dit mag alleen gebeuren als deze halte nog niet 
         * eerder toegevoegd is.
         * @param lijn De toe te voegen lijn, mag niet null zijn
         */
	public void AddLijn(Lijn lijn) {
		throw new UnsupportedOperationException();
	}

        /**
         * Haal alle lijnen op die bij deze halte stoppen
         * @return Een lijst van haltes die bij deze lijn stoppen
         */
	public List<Lijn> HaalLijnen() {
		throw new UnsupportedOperationException();
	}
}
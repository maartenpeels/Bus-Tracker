package com.busenzo.domein;

import java.util.ArrayList;
import com.busenzo.administratie.Administratie;
import java.util.List;

public class Lijn {
	private int id;
	private int nummer;
	private Boolean richting;
	private String beschrijving;
	public ArrayList<Rit> unnamed_Rit_ = new ArrayList<Rit>();
	public ArrayList<Halte> unnamed_Halte_ = new ArrayList<Halte>();
	public Administratie unnamed_Administratie_;

        /**
         * Maak een nieuwe lijn aan. Er mag nog geen lijn bestaan met hetzelfde nummer, richting en beschrijving.
         * @param nummer: Het lijnnummer
         * @param richting: De richting van de lijn
         * @param beschrijving Beschrijving van de lijn.
         */
	public Lijn(Object nummer, Object richting, Object beschrijving) {
		throw new UnsupportedOperationException();
	}

        /**
         * Voeg een halte toe aan deze lijn
         * @param halte: De toe te voegen halte, deze halte mag bij deze lijn nog niet bestaan.
         */
	public void addHalte(Halte halte) {
		throw new UnsupportedOperationException();
	}

        /**
         * Voeg een lijst van haltes toe aan deze lijn
         * @param haltes: Een lijst van de toe te voegen haltes, iedere individuele halte mag bij deze lijn
         * nog niet bestaan
         */
	public void addHaltes(Halte[] haltes) {
		throw new UnsupportedOperationException();
	}

        /**
         * Haal de ritten op die bij deze lijn horen.
         * @return Een lijst van ritten die bij deze lijn horen
         */
	public List<Rit> haalRitten() {
		throw new UnsupportedOperationException();
	}

        /**
         * Voeg een rit toe aan de lijn.
         * @param rit De toe te voegen rit. Deze rit mag bij deze lijn nog niet bestaan.
         */
	public void addRit(Rit rit) {
		throw new UnsupportedOperationException();
	}
}
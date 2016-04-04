package com.busenzo.administratie;

import java.util.ArrayList;
import com.busenzo.domein.Bus;
import com.busenzo.domein.Lijn;
import com.busenzo.domein.Halte;
import java.util.List;

public class Administratie {
    
	public ArrayList<Bus> bussen = new ArrayList<>();
	public ArrayList<Lijn> lijnen = new ArrayList<>();
	public ArrayList<Halte> haltes = new ArrayList<>();

        /**
         * Maak een nieuwe administratie aan
         */
	public Administratie() {
		throw new UnsupportedOperationException();
	}
        /**
         * Zoek naar een specifieke bus
         * @param id: ID (chassisnummer) van de bus
         * @return De bus met het ingevoerde id, null als deze niet bestaat
         */
	public Bus zoekBus(int id) {
		throw new UnsupportedOperationException();
	}

        /**
         * Update de locaties van alle bussen (deze worden opgehaald uit de database)
         */
	public void HaalBusLocaties() {
		throw new UnsupportedOperationException();
	}
        
        /**
         * Haal alle lijnen op die bij een specifieke halte stoppen
         * @param halteNaam: De haltenaam waarvan je de lijnen wil weten, mag geen lege string zijn.
         * @return Een lijst van alle lijnen die bij deze halte stoppen, kan een lege lijst zijn als de halte 
         * niet gevonden wordt
         */
	public List<Lijn> GeefHalteInformatie(String halteNaam) {
		throw new UnsupportedOperationException();
	}

        /**
         * Haal de haltes op van een specifieke lijn
         * @param nummer: Het lijnnummer waarvan je de haltes wil weten.
         * @return Een lijst van haltes waar deze lijn stopt, kan leeg zijn als het lijnnummer niet gevonden wordt
         */
	public List<Halte> GeefLijnInformatie(String nummer) {
		throw new UnsupportedOperationException();
	}
}
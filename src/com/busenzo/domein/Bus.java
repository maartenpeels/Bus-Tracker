package com.busenzo.domein;

import com.busenzo.administratie.Administratie;
import java.util.ArrayList;

public class Bus {
	private int nummer;
	private int lon;
	private int lat;
	public Rit unnamed_Rit_;
	public ArrayList<Melding> unnamed_Melding_ = new ArrayList<Melding>();
	public Administratie unnamed_Administratie_;

        /**
         * Maak een nieuwe bus aan met een specifiek chassisnummer. Er mag nog geen bus bestaan met het gegeven nummer
         * @param nummer Het chassisnummer dat deze bus moet krijgen
         */
	public Bus(Object nummer) {
		throw new UnsupportedOperationException();
	}
        
        /**
         * Update de locatie van deze bus
         * @param lon: De nieuwe longditude van deze bus, mag niet null zijn
         * @param lat: De nieuwe ladditude van deze bus, mag niet null zijn
         */
	public void updateLocatie(Object lon, Object lat) {
		throw new UnsupportedOperationException();
	}
}
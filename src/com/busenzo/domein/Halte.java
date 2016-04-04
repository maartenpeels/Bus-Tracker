package com.busenzo.domein;

import java.util.ArrayList;
import java.util.List;

public class Halte {
    
    private String naam;
    private double lon;
    private double lat;
    private String id;
    public ArrayList<Lijn> lijnen = new ArrayList<>();

    /**
     * Maak een nieuwe halte aan, er mag nog geen halte bestaan met dezelfde naam en lat/lon
     * @param naam: Naam van de halte, mag geen lege string zijn
     * @param lon: Longditude van de halte, mag niet null zijn
     * @param lat: Ladditude van de halte, mag niet null zijn
     */
    public Halte(String id, String naam, String lon, String lat) {
            this.naam = naam;
            this.lat = Double.parseDouble(lat);
            this.lon = Double.parseDouble(lon);
            this.id = id;
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

    /**
     * Vraag de haltenaam op
     * @return De naam van deze halte
     */
    public String getNaam(){
        return this.naam;
    }

    /**
     * Vraag de coordinaten op van deze halte
     * @return Een int array met hierin lon en lat
     */
    public double[] getCoordinaten(){
        double[] cords = {this.lat, this.lon};
        return cords;
    }
}
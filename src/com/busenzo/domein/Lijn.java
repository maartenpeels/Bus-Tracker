package com.busenzo.domein;

import java.util.ArrayList;
import java.util.List;

public class Lijn {
    
    private String id;
    private int nummer;
    private Richting richting;
    private String beschrijving;
    public ArrayList<Rit> ritten = new ArrayList<>();
    public ArrayList<Halte> haltes = new ArrayList<>();

    /**
     * Maak een nieuwe lijn aan. Er mag nog geen lijn bestaan met hetzelfde nummer, richting en beschrijving.
     * @param nummer: Het lijnnummer
     * @param richting: De richting van de lijn
     * @param beschrijving Beschrijving van de lijn.
     */
    public Lijn(String id, int nummer, Richting richting, String beschrijving, ArrayList<Halte> haltes) {
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

    /**
     * Vraag het lijnnummer op
     * @return Het lijnnummer van deze lijn
     */
    public int getNummer() {
        return nummer;
    }

    /**
     * Vraag de richting op van de lijn (heen of terug)
     * @return De richting van de lijn
     */
    public Richting getRichting() {
        return richting;
    }

    /**
     * Vraag de beschrijvin van de lijn op
     * @return De lijnbeschrijving
     */
    public String getBeschrijving() {
        return beschrijving;
    }
}
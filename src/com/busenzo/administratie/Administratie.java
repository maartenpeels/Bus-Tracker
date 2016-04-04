package com.busenzo.administratie;

import java.util.ArrayList;
import com.busenzo.domein.Bus;
import com.busenzo.domein.Lijn;
import com.busenzo.domein.Halte;
import java.util.Collections;
import java.util.List;

public class Administratie {
    
    public ArrayList<Bus> bussen = new ArrayList<>();
    public ArrayList<Lijn> lijnen = new ArrayList<>();
    private ArrayList<Halte> haltes = new ArrayList<>();

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

    /**
     * Voeg een nieuwe bus toe aan de administratie
     * @param bus: De toe te voegen bus, er mag nog geen bus bestaan met hetzelfde chassisnummer 
     */
    public void addBus(Bus bus){
        throw new UnsupportedOperationException();
    }

    /**
     * Voeg meerdere nieuwe bussen toe aan de administratie
     * @param bussen: Een lijst met bussen die toegevoegd worden. Bussen worden alleen toegevoegd als er nog
     * geen bus bestaat met dat specifieke chassisnummer
     */
    public void addBussen(List<Bus> bussen){
        throw new UnsupportedOperationException();
    }

    /**
     * Verwijder een specifieke bus uit de administratie
     * @param bus De te verwijderen bus
     */
    public void removeBus(Bus bus){
        throw new UnsupportedOperationException();
    }

    /**
     * Voeg een halte toe aan de administratie
     * @param halte De toe te voegen halte, deze wordt alleen toegevoegd als deze halte nog niet bestaat
     */
    public void addHalte(Halte halte){
        throw new UnsupportedOperationException();
    }

    /**
     * Voeg meerdere haltes toe aan de administratie
     * @param haltes Een lijst van toe te voegen haltes, haltes worden alleen toegevoegd als deze nog niet 
     * bestaan
     */
    public void addHaltes(List<Halte> haltes){
        throw new UnsupportedOperationException();
    }

    /**
     * Voeg een specifieke lijn toe aan de administratie
     * @param lijn De toe te voegen lijn. Deze wordt alleen toegevoegd als deze lijn nog niet bestaat.
     */
    public void addLijn(Lijn lijn){
        throw new UnsupportedOperationException();
    }

    /**
     * Voeg meerdere lijnene toe aan de administratie
     * @param lijnen De toe te voegen lijnen. Lijnen worden alleen toegevoegd als ze nog niet bestaan
     */
    public void addLijnen(List<Lijn> lijnen){
        throw new UnsupportedOperationException();
    }

    /**
     * haal de lijst van haltes op
     * @return een onwijzigbare lijst van haltes
     */
    public List<Halte> getHaltes() {
        return Collections.unmodifiableList(haltes);
    }
          
}
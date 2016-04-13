package com.busenzo.domein;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lijn {

    private final String id;
    private final int nummer;
    private final Richting richting;
    private final String beschrijving;
    private ArrayList<Rit> ritten;
    private ArrayList<Halte> haltes;

    /**
     * Maak een nieuwe lijn aan. Er mag nog geen lijn bestaan met hetzelfde
     * nummer, richting en beschrijving.
     *
     * @param id: LijnID geleverd door ov database
     * @param nummer: Het lijnnummer
     * @param richting: De richting van de lijn
     * @param beschrijving Beschrijving van de lijn.
     */
    public Lijn(String id, int nummer, Richting richting, String beschrijving) {
        this.id = id;
        this.nummer = nummer;
        this.richting = richting;
        this.beschrijving = beschrijving;
        this.haltes = new ArrayList<>();
        this.ritten = new ArrayList<>();
    }

    /**
     * Voeg een halte toe aan deze lijn
     *
     * @param halte: De toe te voegen halte, deze halte mag bij deze lijn nog
     * niet bestaan.
     * @return true als de halte succesvol is toegevoegd, anders false
     */
    public boolean addHalte(Halte halte) {
        for (Halte h : haltes) {
            if (h.getId().equals(halte.getId())) {
                return false;
            }
        }

        this.haltes.add(halte);
        return true;
    }

    /**
     * Voeg een lijst van haltes toe aan deze lijn
     *
     * @param haltes: Een lijst van de toe te voegen haltes, iedere individuele
     * halte mag bij deze lijn nog niet bestaan
     * @return true als de halte is toegevoegd, anders false
     */
    public boolean addHaltes(Halte[] haltes) {
        for (Halte h1 : this.haltes) {
            for (Halte h2 : haltes) {
                if (h1.getId().equals(h2.getId())) {
                    return false;
                }

            }
        }

        this.haltes.addAll(Arrays.asList(haltes));
        return true;
        

    }

    /**
     * Haal de ritten op die bij deze lijn horen.
     *
     * @return Een lijst van ritten die bij deze lijn horen
     */
    public List<Rit> getRitten() {
        return this.ritten;
    }
    
    public ArrayList<String> getHalteNamen(){
        ArrayList<String> halteNamen = new ArrayList<String>();
        for (Halte i : haltes){
            halteNamen.add(i.getNaam());
        }
        return halteNamen;
    }

    /**
     * Voeg een rit toe aan de lijn.
     *
     * @param rit De toe te voegen rit. Deze rit mag bij deze lijn nog niet
     * bestaan, en de rit mag ook niet bij een andere lijn horen.
     * @return true als de rit is toegevoegd, anders false
     */
    public boolean addRit(Rit rit) {
        for(Rit r : ritten)
        {
            if(r.getLijn().getId().equals(rit.getLijn().getId()) && r.getVerwachteAankomstTijd() == rit.getVerwachteAankomstTijd()) {
                return false;
            }
        }
        if(rit.getLijn() != null && !rit.getLijn().getId().equals(this.id)){
            return false;
        }
        this.ritten.add(rit);
        return true;
    }

    /**
     * Vraag het lijnnummer op
     *
     * @return Het lijnnummer van deze lijn
     */
    public int getNummer() {
        return nummer;
    }

    public String getId() {
        return this.id;
    }

    /**
     * Vraag de richting op van de lijn (heen of terug)
     *
     * @return De richting van de lijn
     */
    public Richting getRichting() {
        return richting;
    }

    /**
     * Vraag de beschrijvin van de lijn op
     *
     * @return De lijnbeschrijving
     */
    public String getBeschrijving() {
        return beschrijving;
    }
    
    /**
     * leeg de lijst van ritten
     */
    public void clearRitten(){
        this.ritten.clear();
    }
}

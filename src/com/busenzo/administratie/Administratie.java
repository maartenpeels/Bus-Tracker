package com.busenzo.administratie;

import java.util.ArrayList;
import com.busenzo.domein.Bus;
import com.busenzo.domein.Lijn;
import com.busenzo.domein.Halte;
import com.busenzo.domein.Melding;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Administratie {

    private String restServer = "http://37.97.149.53/busenzo/api/";
    //private String restServer = "https://busenzo.stefanvlems.nl/api/";
    private String restKey = "9709d02bfb3b1460fd0dd45f6706a81a8c33afaf";
    private ArrayList<Bus> bussen;
    private ArrayList<Lijn> lijnen;
    private ArrayList<Halte> haltes;
    private ArrayList<Melding> meldingen;
    private DatabaseKoppeling dbKoppeling;

    /**
     * Maak een nieuwe administratie aan
     */
    public Administratie() {
        this.bussen = new ArrayList<>();
        this.lijnen = new ArrayList<>();
        this.haltes = new ArrayList<>();
        this.meldingen = new ArrayList<>();
        this.dbKoppeling = new DatabaseKoppeling(restServer, restKey);
    }

    public void laadDataIn() {
        try {
            this.haltes.addAll(dbKoppeling.getHalteData());
            this.lijnen.addAll(dbKoppeling.getLineData(haltes));
            this.dbKoppeling.getRouteData(lijnen);
            this.meldingen.addAll(this.dbKoppeling.getMeldingen());
        } catch (Exception ex) {
            Logger.getLogger(Administratie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Zoek naar een specifieke bus
     *
     * @param id: ID (chassisnummer) van de bus
     * @return De bus met het ingevoerde id, null als deze niet bestaat
     */
    public Bus zoekBus(int id) {
        for (Bus b : this.bussen) {
            if (b.getNummer() == id) {
                return b;
            }
        }
        return null;
    }

    /**
     * Update de locaties van alle bussen (deze worden opgehaald uit de
     * database)
     */
    public void HaalBusLocaties() {
        this.lijnen.clear();
        try {
            this.lijnen.addAll(dbKoppeling.getLineData(haltes));
            this.dbKoppeling.getRouteData(lijnen);
        } catch (Exception ex) {
            Logger.getLogger(Administratie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Haal alle lijnen op die bij een specifieke halte stoppen
     *
     * @param halteNaam: De haltenaam waarvan je de lijnen wil weten, mag geen
     * lege string zijn.
     * @return Een lijst van alle lijnen die bij deze halte stoppen, kan een
     * lege lijst zijn als de halte niet gevonden wordt
     */
    public List<Lijn> GeefHalteInformatie(String halteNaam) {
        if (halteNaam.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            for (Halte h : this.haltes) {
                if (h.getNaam().equals(halteNaam)) {
                    return h.HaalLijnen();
                }
            }
        }
        return null;
    }

    /**
     * Haal de haltes op van een specifieke lijn
     *
     * @param nummer: Het lijnnummer waarvan je de haltes wil weten.
     * @return Een lijst van haltes waar deze lijn stopt, kan leeg zijn als het
     * lijnnummer niet gevonden wordt
     */
    public List<Halte> GeefLijnInformatie(String nummer) {
        throw new UnsupportedOperationException();
    }

    /**
     * Voeg een nieuwe bus toe aan de administratie
     *
     * @param bus: De toe te voegen bus, er mag nog geen bus bestaan met
     * hetzelfde chassisnummer
     */
    public void addBus(Bus bus) {
        throw new UnsupportedOperationException();
    }

    /**
     * Voeg meerdere nieuwe bussen toe aan de administratie
     *
     * @param bussen: Een lijst met bussen die toegevoegd worden. Bussen worden
     * alleen toegevoegd als er nog geen bus bestaat met dat specifieke
     * chassisnummer
     */
    public void addBussen(List<Bus> bussen) {
        throw new UnsupportedOperationException();
    }

    /**
     * Verwijder een specifieke bus uit de administratie
     *
     * @param bus De te verwijderen bus
     */
    public void removeBus(Bus bus) {
        throw new UnsupportedOperationException();
    }

    /**
     * Voeg een halte toe aan de administratie
     *
     * @param halte De toe te voegen halte, deze wordt alleen toegevoegd als
     * deze halte nog niet bestaat
     */
    public void addHalte(Halte halte) {
        if (!haltes.contains(halte)) {
            this.haltes.add(halte);
        }
    }

    /**
     * Voeg meerdere haltes toe aan de administratie
     *
     * @param haltes Een lijst van toe te voegen haltes, haltes worden alleen
     * toegevoegd als deze nog niet bestaan
     */
    public void addHaltes(List<Halte> haltes) {
        for (Halte h : haltes) {
            if (!this.haltes.contains(h)) {
                this.haltes.add(h);
            }
        }
    }

    /**
     * Voeg een specifieke lijn toe aan de administratie
     *
     * @param lijn De toe te voegen lijn. Deze wordt alleen toegevoegd als deze
     * lijn nog niet bestaat.
     */
    public void addLijn(Lijn lijn) {
        throw new UnsupportedOperationException();
    }

    /**
     * Voeg meerdere lijnene toe aan de administratie
     *
     * @param lijnen De toe te voegen lijnen. Lijnen worden alleen toegevoegd
     * als ze nog niet bestaan
     */
    public void addLijnen(List<Lijn> lijnen) {
        throw new UnsupportedOperationException();
    }

    /**
     * haal de lijst van haltes op
     *
     * @return een onwijzigbare lijst van haltes
     */
    public List<Halte> getHaltes() {
        return Collections.unmodifiableList(haltes);
    }

    /**
     * haal een lijst van bussen op
     *
     * @return onwijzigbare lijst van bussen
     */
    public List<Lijn> getBussen() {
        return Collections.unmodifiableList(lijnen);
    }

    public List<Lijn> zoekLijn(String naam) {
        ArrayList<Lijn> output = new ArrayList<>();
        for (Lijn l : this.lijnen) {
            String busNumber = String.valueOf(l.getNummer());
            if (busNumber.equals(naam)) {
                output.add(l);
            }
        }
        return output;
    }

    public List<Halte> zoekHalte(String naam) {
        ArrayList<Halte> output = new ArrayList<>();
        if (naam.length() > 0) {
            for (Halte h : this.haltes) {
                if (h.getNaam().toLowerCase().contains(naam.toLowerCase())) {
                    output.add(h);
                }
            }
        }

        return output;
    }
    public List<Melding> getAllMeldingen() {
        return this.meldingen;
    }
    /**
     * haal de lijst van meldingen op na een bepaald id
     *
     * @return een onwijzigbare lijst van meldingen
     */
    /*
    public List<Melding> getMeldingen(int laatsteId, int van, int naar) throws Exception {
        ArrayList<Melding> output = new ArrayList<>();
        String query = "";

        if (van == -1) {
            query = "meldingen&to=" + naar + "&last=" + laatsteId;
        } else if (naar == -1) {
            query = "meldingen&from=" + van + "&last=" + laatsteId;
        } else {
            return null;
        }

        JSONObject meldingData = dbKoppeling.getJSONfromWeb(query);
        JSONArray meldingArray = (JSONArray) meldingData.get("data");
        for (Object meldingArray1 : meldingArray) {
            JSONObject objects = (JSONObject) meldingArray1;

            String beschrijving = objects.get("message").toString();

            String tijd = objects.get("time").toString();
            DateTimeFormatter frm = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime vat = LocalDateTime.parse(tijd, frm);

            String van1 = objects.get("from").toString();

            String naar1 = objects.get("to").toString();
            
            if (van == -1) {
                Melding m = new Melding(beschrijving, "", naar1, vat);
                output.add(m);
            } else if (naar == -1) {
                Melding m = new Melding(beschrijving, van1, "", vat);
                output.add(m);
            }
        }

        return output;
    }
*/
}

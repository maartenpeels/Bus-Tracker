package administratie;

import administratie.DatabaseKoppeling;
import java.util.ArrayList;
import domein.Halte;
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
    private ArrayList<Halte> haltes;
    private DatabaseKoppeling dbKoppeling;

    /**
     * Maak een nieuwe administratie aan
     */
    public Administratie() {
        this.haltes = new ArrayList<>();
        this.dbKoppeling = new DatabaseKoppeling(restServer, restKey);
    }

    public void laadDataIn() {
        try {
            this.haltes.addAll(dbKoppeling.getHalteData());
        } catch (Exception ex) {
            Logger.getLogger(Administratie.class.getName()).log(Level.SEVERE, null, ex);
        }
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
}
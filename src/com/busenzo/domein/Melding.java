package com.busenzo.domein;

import java.io.Serializable;
import java.rmi.Remote;
import java.time.LocalDateTime;
import java.util.Date;

public class Melding implements Serializable, Remote {
    private Integer meldingID;
    private LocalDateTime tijdstip;
    private String beschrijving;
    private Boolean actief;
    private String zender;
    private String ontvanger;
    private Categorie categorie;
    
    private static final long serialVersionUID = 1L;

    /**
     * Maak een nieuwe melding aan. Ontvanger of zender is altijd -1.
     * @param beschrijving: Beschrijving van de melding, mag geen lege string zijn
     * @param zender: De zender van de melding, -1 als de melding van het beheer naar een bus is
     * @param ontvanger: De ontvanger van de melding, -1 als de melding van een bus naar het beheer is 
     */
    public Melding(Integer meldingID, String beschrijving, String zender, String ontvanger, LocalDateTime tijd) {
            this.meldingID = meldingID;
            this.beschrijving = beschrijving;
            this.zender = zender;
            this.ontvanger = ontvanger;
            this.actief = true;
            this.tijdstip = tijd;
    }

    /**
     * Kijk of deze melding nog actief is
     * @return True als de melding actief is, anders false
     */
    public Boolean getActief() {
            return this.actief;
    }

    /**
     * Verander de status van de melding
     * @param actief True als de melding nog actief is, false als dit niet meer zo is
     */
    public void setActief(Boolean actief) {
            this.actief = actief;
    }

    /**
     * Vraag het tijdstip van de melding op
     * @return Het tijdstip waarop de melding is gedaan
     */
    public LocalDateTime getTijdstip() {
        return tijdstip;
    }
    
    /**
     * Vraag de beschrijving op van de melding
     * @return De beschrijving van de melding (het daadwerkelijke bericht)
     */
    public String getBeschrijving() {
        return beschrijving;
    }
    
    /**
     * Vraag de verzender van de melding op
     * @return Het chassisnummer (id) van de bus welke de melding verzonden heeft, of -1 als de melding
     * door de beheerder is gedaan
     */
    public String getSender() {
        return zender;
    }
    
    public Integer getID() {
        return meldingID;
    }
    
    /**
     * Vraag de ontvanger van de melding op
     * @return Het chassisnummer (id) van de bus welke de melding ontvangen heeft, of -1 als de ontvanger
     * de beheerder is
     */
    public String getReceiver() {
        return ontvanger;
    }
    
    /**
     * Vraag de categorie op waar deze melding onder valt
     * @return De categorie van deze melding
     */
    public Categorie getCategorie() {
        return categorie;
    }
}

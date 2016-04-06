package com.busenzo.domein;

import java.time.LocalDateTime;
import java.util.Date;

public class Rit {
    
    private LocalDateTime verwachteAankomstTijd;
    private LocalDateTime aankomstTijd;
    private Bus bus;
    private Lijn lijn;

    /**
     * Maak een rit aan op een bepaalde lijn
     * @param verwachtteAankomstTijd De verwachtte aankomsttijd bij de eindhalte
     * @param lijn: De lijn waarbij deze rit hoort, nooit null
     */
    public Rit(LocalDateTime verwachtteAankomstTijd, Lijn lijn) {
        this.lijn = lijn;
        this.verwachteAankomstTijd = verwachtteAankomstTijd;
    }

    /**
     * Haal de bus op die deze rit rijdt. 
     * @return De bus die deze rit rijdt. Kan null zijn als de rit nog niet vertrokken is
     */
    public Bus getBus() {
        return this.bus;
    }
    
    public void setAankomstTijd(LocalDateTime aankomstTijd)
    {
        this.aankomstTijd = aankomstTijd;
    }

    /**
     * Haal de lijn op waarbij deze rit hoort
     * @return De lijn waarop deze rit rijdt 
     */
    public Lijn getLijn() {
        return this.lijn;
    }

    /**
     * Haal de verwachtte aankomsttijd bij de eindhalte op
     * @return de verwachtte aankomsttijd bij de eindhalte
     */
    public LocalDateTime getVerwachteAankomstTijd() {
        return verwachteAankomstTijd;
    }

    /**
     * Vraag de verwachtte aankomsttijd op bij de volgende halte
     * @return De verwachtte aankomsttijd bij de volgende halte
     */
    public LocalDateTime getAankomstTijd() {
        return aankomstTijd;
    }

    /**
     * Voeg een bus toe welke deze rit gaat rijden. 
     * @param bus : De bus die deze rit gaat rijden, mag niet null zijn
     */
    public void setBus(Bus bus) {
        this.bus = bus;
    }

}
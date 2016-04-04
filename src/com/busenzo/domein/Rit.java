package com.busenzo.domein;

import java.util.Date;

public class Rit {
    
    private Date vertrekTijd;
    private Date verwachteAankomstTijd;
    private Date aankomstTijd;
    private Bus bus;
    private Lijn lijn;

    /**
     * Maak een rit aan op een bepaalde lijn met een specifieke vertrektijd
     * @param vertrekTijd De vertrektijd bij de eerste halte, nooit null
     * @param verwachtteAankomstTijd De verwachtte aankomsttijd bij de eindhalte
     * @param lijn: De lijn waarbij deze rit hoort, nooit null
     */
    public Rit(Date vertrekTijd, Date verwachtteAankomstTijd, Lijn lijn) {
        throw new UnsupportedOperationException();
    }

    /**
     * Haal de vertrektijd van deze rit op
     * @return Vertrektijd van de rit bij de eerste halte
     */
    public Date getVertrekTijd() {
        return this.vertrekTijd;
    }

    /**
     * Haal de bus op die deze rit rijdt. 
     * @return De bus die deze rit rijdt. Kan null zijn als de rit nog niet vertrokken is
     */
    public Bus getBus() {
        return this.bus;
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
    public Date getVerwachteAankomstTijd() {
        return verwachteAankomstTijd;
    }

    /**
     * Vraag de verwachtte aankomsttijd op bij de volgende halte
     * @return De verwachtte aankomsttijd bij de volgende halte
     */
    public Date getAankomstTijd() {
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
package com.busenzo.domein;

import java.util.Date;

public class Rit {
	private Date vertrekTijd;
	private Date verwachteAankomstTijd;
	private Date aankomstTijd;
	private Bus bus;
	private Lijn lijn;
	public Bus voertuig;
	public Lijn unnamed_Lijn_;

        /**
         * Maak een rit aan op een bepaalde lijn met een specifieke vertrektijd
         * @param vertrekTijd De vertrektijd bij de eerste halte, nooit null
         * @param lijn: De lijn waarbij deze rit hoort, nooit null
         */
	public Rit(Object vertrekTijd, Object lijn) {
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
         * Haal de bus op die deze rit rijdt. Kan null zijn als de rit nog niet vertrokken is
         */
	public void getBus() {
		throw new UnsupportedOperationException();
	}

        /**
         * Voeg de bus toe die deze rit rijdt. Moet gebeuren voor de vertrektijd van deze rit.
         * @param bus De bus die deze rit rijdt.
         */
	public void setBus(Object bus) {
		throw new UnsupportedOperationException();
	}

        /**
         * Haal de lijn op waarbij deze rit hoort
         */
	public void getLijn() {
		throw new UnsupportedOperationException();
	}

        /**
         * Zet de lijn waarbij deze rit hoort.
         * @param lijn De lijn waarbij deze rit hoort. Nooit null.
         */
	public void setLijn(Object lijn) {
		throw new UnsupportedOperationException();
	}
}
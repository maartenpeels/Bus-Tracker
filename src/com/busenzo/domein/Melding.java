package com.busenzo.domein;

import java.util.Date;

public class Melding {
	private Date tijdstip;
	private String beschrijving;
	private Boolean actief;
	private Integer zender;
	private Integer ontvanger;
	public Categorie unnamed_Categorie_;
	public Bus unnamed_Bus_;

        /**
         * Maak een nieuwe melding aan. Ontvanger of zender is altijd -1.
         * @param beschrijving: Beschrijving van de melding, mag geen lege string zijn
         * @param zender: De zender van de melding, -1 als de melding van het beheer naar een bus is
         * @param ontvanger: De ontvanger van de melding, -1 als de melding van een bus naar het beheer is 
         */
	public Melding(String beschrijving, int zender, Object ontvanger) {
		throw new UnsupportedOperationException();
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
}

package com.busenzo.domein;

import java.util.Date;

public class Melding {
	private Date tijdstip;
	private String beschrijving;
	private Boolean actief;
	private Integer zender;
	private Integer ontvanger;
	public Categorie categorie;
	public Bus bus;

	public Melding(String beschrijving, int zender, int ontvanger) {
		throw new UnsupportedOperationException();
	}

	public Boolean getActief() {
		return this.actief;
	}

	public void setActief(Boolean actief) {
		this.actief = actief;
	}
}
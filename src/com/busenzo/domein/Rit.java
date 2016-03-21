package com.busenzo.domein;

import java.util.Date;

public class Rit {
	private Date vertrekTijd;
	private Date verwachteAankomstTijd;
	private Date aankomstTijd;
	public Bus voertuig;
	public Lijn lijn;

	public Rit(Date vertrekTijd, Lijn lijn) {
		throw new UnsupportedOperationException();
	}

	public Date getVertrekTijd() {
		return this.vertrekTijd;
	}
}
package org.inftel.tms.mobile.pasos;

import java.util.Calendar;

public class AbstractPasosTrasmitter {
	protected String senderNumber;
	protected String date = "";
	protected String time = "";

	
	public AbstractPasosTrasmitter() {
		super();
	}

	public String getSenderNumber() {
		return senderNumber;
	}

	public void setSenderNumber(String senderNumber) {
		this.senderNumber = senderNumber;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	/**
	 * Establece "Date" y "Time" en el formato que indica el protocolo PASOS. Les asigna la fecha y
	 * hora actuales.
	 */
	public void setDateandTime() {
		Calendar calendar = Calendar.getInstance();
				
		String dayString = String.format("%td", calendar );
		String monthString = String.format("%tm", calendar );
		String hourString = String.format("%tH", calendar );
		String minsString = String.format("%tM", calendar );
		String secsString = String.format("%tS", calendar );
		String yearString = String.valueOf(calendar.get(Calendar.YEAR));
		
		this.date = "&LD" + yearString + monthString + dayString;
		this.time = "&LH" + hourString + minsString + secsString;
	}
}

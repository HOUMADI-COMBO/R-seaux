package fr.ensisa.hassenforder.tp.client.network;

import java.util.Date;

public class TextOperationRequest {

	static public enum What {
		COMMENT,
		DELETE,
		INSERT,
	}

	private final Date date;
	private final What what;
	private final int where;
	private final String text;

	public TextOperationRequest(Date date, What what, int where, String text) {
		super();
		this.date = date;
		this.what = what;
		this.where = where;
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public What getWhat() {
		return what;
	}

	public int getWhere() {
		return where;
	}

	public String getText() {
		return text;
	}

}

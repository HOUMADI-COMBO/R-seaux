package fr.ensisa.hassenforder.tp.database;

import java.util.Date;

public class Operation {

	private long id;
	private Date date;
	private long who;
	private long on;
	private What what;
	private int where;
	private String text;

	public Operation(Date date, long who, long on, What what, int where, String text) {
		super();
		this.date = date;
		this.who = who;
		this.on = on;
		this.what = what;
		this.where = where;
		this.text = text;
	}

	public Operation(Date date, What what, int where, String text) {
		this(date, 0, 0, what, where, text);
	}

	public long getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public long getWho() {
		return who;
	}

	public long getOn() {
		return on;
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

	protected void setId(long id) {
		this.id = id;
	}

	public void setWho(long who) {
		this.who = who;
	}

	public void setOn(long on) {
		this.on = on;
	}

}

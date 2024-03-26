package fr.ensisa.hassenforder.tp.server.network;

import java.util.Date;

import fr.ensisa.hassenforder.tp.database.What;

public class OperationReply {

	private long id;
	private Date date;
	private String name;
	private What what;
	private int where;
	private String text;

	public OperationReply(long id, Date date, String name, What what, int where, String text) {
		super();
		this.id = id;
		this.date = date;
		this.name = name;
		this.what = what;
		this.where = where;
		this.text = text;
	}

	public long getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public String getName() {
		return name;
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

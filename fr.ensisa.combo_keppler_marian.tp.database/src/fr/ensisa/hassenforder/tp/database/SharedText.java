package fr.ensisa.hassenforder.tp.database;

import java.util.Date;

public class SharedText {

	private long id;
	private Date created;
	private String content;

	public SharedText(Date created, String content) {
		super();
		this.created = created;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	protected void setId(long id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}

package fr.ensisa.hassenforder.tp.server.network;

import java.util.Date;

import fr.ensisa.hassenforder.tp.database.Role;

public class SharedTextReply {

	private long id;
	private Date date;
	private String content;
	private Role role;
	private String owner;

	public SharedTextReply(long id, Date date, String content, Role role, String owner) {
		super();
		this.id = id;
		this.date = date;
		this.content = content;
		this.role = role;
		this.owner = owner;
	}

	public long getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public String getContent() {
		return content;
	}

	public Role getRole() {
		return role;
	}

	public String getOwner() {
		return owner;
	}

}

package fr.ensisa.hassenforder.tp.server.network;

import fr.ensisa.hassenforder.tp.database.Role;

public class ParticipantReply {

	private long id;
	private String name;
	private Role role;

	public ParticipantReply(long id, String name, Role role) {
		super();
		this.id = id;
		this.name = name;
		this.role = role;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Role getRole() {
		return role;
	}

}

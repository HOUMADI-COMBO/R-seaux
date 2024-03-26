package fr.ensisa.hassenforder.tp.client.network;

import fr.ensisa.hassenforder.tp.client.model.Role;

public class ParticipantOperationRequest {

	private final long who;
	private final Role role;

	public ParticipantOperationRequest(long who, Role role) {
		super();
		this.who = who;
		this.role = role;
	}

	public long getWho() {
		return who;
	}

	public Role getRole() {
		return role;
	}


}

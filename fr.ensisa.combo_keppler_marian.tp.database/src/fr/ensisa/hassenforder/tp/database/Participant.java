package fr.ensisa.hassenforder.tp.database;

public class Participant {

	private long id;
	private long who;
	private long on;
	private Role role;

	public Participant(long who, long on, Role role) {
		super();
		this.who = who;
		this.on = on;
		this.role = role;
	}

	public Participant(long who, Role role) {
		this(who, 0, role);
	}

	public long getId() {
		return id;
	}

	public long getWho() {
		return who;
	}

	public long getOn() {
		return on;
	}

	public Role getRole() {
		return role;
	}

	protected void setId(long id) {
		this.id = id;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setOn(long on) {
		this.on = on;
	}

}

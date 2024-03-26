package fr.ensisa.hassenforder.tp.client.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Participant {

	private final LongProperty id;
	private final StringProperty name;
	private final StringProperty roleProperty;
	private Role role;

	public Participant(long id, String name, Role role) {
		super();
		this.id = new SimpleLongProperty(id);
		this.name = new SimpleStringProperty(name);
		this.role = role;
		this.roleProperty = new SimpleStringProperty((role != null) ? role.asString() : "");
	}

	public LongProperty getId() {
		return id;
	}

	public StringProperty getName() {
		return name;
	}

	public Role getRole() {
		return role;
	}

	public StringProperty getRoleProperty () {
		return roleProperty;
	}

}

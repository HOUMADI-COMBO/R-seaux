package fr.ensisa.hassenforder.tp.client.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {

	private final LongProperty id;
	private final StringProperty name;
	private final StringProperty token;
	private final StringProperty state;

	public User(long id, String name, String token) {
		super();
		this.id = new SimpleLongProperty(id);
		this.name = new SimpleStringProperty(name);
		this.token = new SimpleStringProperty(token);
		this.state = new SimpleStringProperty("");
		this.id.addListener(value -> buildState());
		this.name.addListener(value -> buildState());
		this.token.addListener(value -> buildState());
	}

	private void buildState () {
		if (id.get() == 0) state.set("unknown");
		if (name.get().isEmpty()) state.set("unnamed");
		if (token.get().isEmpty()) state.set("unconnected");
		state.set("connected");
	}

	public long getId() {
		return id.get();
	}

	public String getName() {
		return name.get();
	}

	public String getToken () {
		return token.get();
	}

	public String getState () {
		return state.get();
	}

	public void setId(long id) {
		this.id.set(id);
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public void setToken(String token) {
		this.token.set(token);
	}

	public LongProperty getIdProperty() {
		return id;
	}

	public StringProperty getNameProperty() {
		return name;
	}

	public StringProperty getTokenProperty () {
		return token;
	}

	public StringProperty getStateProperty () {
		return state;
	}

}

package fr.ensisa.hassenforder.tp.client.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Credential {

	private LongProperty id;
	private StringProperty name;
	private StringProperty mail;
	private StringProperty passwd;

	private SimpleStringProperty state;

	public Credential() {
		this (0, "", "", "");
	}

	public Credential(long id, String name, String mail, String passwd) {
		super();
		this.id = new SimpleLongProperty(id);
		this.name = new SimpleStringProperty(name);
		this.mail = new SimpleStringProperty(mail);
		this.passwd = new SimpleStringProperty(passwd);
		this.state = new SimpleStringProperty(buildState());
		this.name.addListener( l -> this.state.set(buildState()));
		this.mail.addListener( l -> this.state.set(buildState()));
		this.state.addListener( l -> this.state.set(buildState()));
	}

	private String buildState() {
		if (name.get().isEmpty()) return "cannot use an empty name";
		if (mail.get().isEmpty()) return "cannot use an empty mail";
		if (passwd.get().isEmpty()) return "cannot use an empty passwd";
		return "valid ... or not";
	}

	public long getId() {
		return id.get();
	}

	public String getName() {
		return name.get();
	}

	public String getMail() {
		return mail.get();
	}

	public String getPasswd() {
		return passwd.get();
	}

	public LongProperty getIdProperty() {
		return id;
	}

	public StringProperty getNameProperty() {
		return name;
	}

	public StringProperty getMailProperty() {
		return mail;
	}

	public StringProperty getPasswdProperty() {
		return passwd;
	}

	public SimpleStringProperty getStateProperty() {
		return state;
	}

}

package fr.ensisa.hassenforder.tp.client.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SharedText {

	private final long id;
	private final StringProperty created;
	private final StringProperty content;
	private final StringProperty role;
	private final StringProperty owner;
	private final IntegerProperty editCount;
	private final IntegerProperty commentCount;

	static private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM hh:mm:ss");

	public SharedText(long id, Date created, String content, Role role, String owner) {
		super();
		this.id = id;
		this.created = new SimpleStringProperty(sdf.format(created));
		this.content = new SimpleStringProperty(content);
		this.role = new SimpleStringProperty(role.asString());
		this.owner = new SimpleStringProperty(owner);
		this.editCount = new SimpleIntegerProperty(0);
		this.commentCount = new SimpleIntegerProperty(0);
	}

	public long getId() {
		return id;
	}

	public StringProperty getCreated() {
		return created;
	}

	public StringProperty getContent() {
		return content;
	}

	public IntegerProperty getEditCount() {
		return editCount;
	}

	public IntegerProperty getCommentCount() {
		return commentCount;
	}

	public StringProperty getRole() {
		return role;
	}

	public StringProperty getOwner() {
		return owner;
	}

}

package fr.ensisa.hassenforder.tp.client.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TextOperation {

	static public enum What {
		COMMENT,
		DELETE,
		INSERT,
	}

	private final long id;
	private final StringProperty date;
	private final StringProperty who;
	private final StringProperty what;
	private final IntegerProperty where;
	private final StringProperty text;

	static private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM hh:mm:ss");

	public TextOperation(long id, Date date, String who, What what, int where, String text) {
		super();
		this.id = id;
		this.date = new SimpleStringProperty(sdf.format(date));
		this.who = new SimpleStringProperty(who);
		this.what = new SimpleStringProperty(what.name().toLowerCase());
		this.where= new SimpleIntegerProperty(where);
		this.text = new SimpleStringProperty(text);
	}

	public long getId() {
		return id;
	}

	public StringProperty getDate() {
		return date;
	}

	public StringProperty getWho() {
		return who;
	}

	public StringProperty getWhat() {
		return what;
	}

	public IntegerProperty getWhere() {
		return where;
	}

	public StringProperty getText() {
		return text;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(what.get());
		builder.append(" @ ");
		builder.append(where.get());
		builder.append(" '");
		builder.append(text.get());
		builder.append("'");
		return builder.toString();
	}

}

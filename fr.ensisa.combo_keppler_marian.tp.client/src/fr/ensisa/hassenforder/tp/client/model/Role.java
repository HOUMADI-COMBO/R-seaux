package fr.ensisa.hassenforder.tp.client.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Role {

	public static final int OWNER 		= 0x08;
	public static final int EDITER 		= 0x04;
	public static final int COMMENTER	= 0x02;
	public static final int READER		= 0x01;

	public static final int ALL			= OWNER | EDITER | COMMENTER | READER;

	private BooleanProperty isOwner;
	private BooleanProperty canEdit;
	private BooleanProperty canComment;
	private BooleanProperty canRead;

	public Role(int role) {
		super();
		isOwner = new SimpleBooleanProperty((role & OWNER) != 0);
		canEdit = new SimpleBooleanProperty((role & EDITER) != 0);
		canComment = new SimpleBooleanProperty((role & COMMENTER) != 0);
		canRead = new SimpleBooleanProperty((role & READER) != 0);
	}

	public BooleanProperty getIsOwner() {
		return isOwner;
	}

	public BooleanProperty getCanEdit() {
		return canEdit;
	}

	public BooleanProperty getCanComment() {
		return canComment;
	}

	public BooleanProperty getCanRead() {
		return canRead;
	}

	public String asString() {
		StringBuilder tmp = new StringBuilder ();
		if (isOwner.get()) tmp.append("O");
		if (canEdit.get()) tmp.append("E");
		if (canComment.get()) tmp.append("C");
		if (canRead.get()) tmp.append("R");
		return tmp.toString();
	}

	public int asInt() {
		int role = 0;
		if (isOwner.get()) role |= OWNER;
		if (canEdit.get()) role |= EDITER;
		if (canComment.get()) role |= COMMENTER;
		if (canRead.get()) role |= READER;
		return role;
	}

}

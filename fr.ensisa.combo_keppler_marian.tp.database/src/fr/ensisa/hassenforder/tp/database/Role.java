package fr.ensisa.hassenforder.tp.database;

public class Role {

	public static final int OWNER 		= 0x08;
	public static final int EDITER 		= 0x04;
	public static final int COMMENTER	= 0x02;
	public static final int READER		= 0x01;

	public static final int ALL			= OWNER | EDITER | COMMENTER | READER;

	private int role;

	public Role(int role) {
		super();
		this.role = role;
	}

	public boolean isCreator() {
		return (role & OWNER) != 0;
	}

	public boolean canEdit() {
		return (role & EDITER) != 0;
	}

	public boolean canComment() {
		return (role & COMMENTER) != 0;
	}

	public boolean canRead() {
		return (role & READER) != 0;
	}

	private void toggle (int r) {
		if ((role & r) != 0) {
			role &= ~ r;
		} else {
			role |= r;
		}
	}

	public void toggleCanEdit () {
		toggle(EDITER);
	}

	public void toggleCanComment () {
		toggle(COMMENTER);
	}

	public void toggleCanRead () {
		toggle(READER);
	}

	public String asString() {
		StringBuilder tmp = new StringBuilder ();
		if ((role & ALL) == ALL) tmp.append("ALL");
		else {
			if ((role & OWNER) != 0) tmp.append("O");
			if ((role & EDITER) != 0) tmp.append("E");
			if ((role & COMMENTER) != 0) tmp.append("C");
			if ((role & READER) != 0) tmp.append("R");
		}
		return tmp.toString();
	}

	public int asInt() {
		return role;
	}

}

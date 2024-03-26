package fr.ensisa.hassenforder.tp.database;

public class User {

	private long id;
	private String name;
	private String mail;
	private String passwd;

	public User(String name, String mail, String passwd) {
		super();
		this.name = name;
		this.mail = mail;
		this.passwd = passwd;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getMail() {
		return mail;
	}

	public String getPasswd() {
		return passwd;
	}

	protected void setId(long id) {
		this.id = id;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

}

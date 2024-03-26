package fr.ensisa.hassenforder.tp.server.network;

public class Credential {

	private String name;
	private String mail;
	private String passwd;

	public Credential(String name, String mail, String passwd) {
		super();
		this.name = name;
		this.mail = mail;
		this.passwd = passwd;
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

}

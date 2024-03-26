package fr.ensisa.hassenforder.tp.server.network;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import fr.ensisa.hassenforder.network.BasicAbstractReader;
import fr.ensisa.hassenforder.tp.database.Operation;
import fr.ensisa.hassenforder.tp.database.Participant;
import fr.ensisa.hassenforder.tp.database.Role;
import fr.ensisa.hassenforder.tp.database.What;
import fr.ensisa.hassenforder.tp.network.Protocol;

public class TCPReader extends BasicAbstractReader {

    String userPasswd;
    String userMail;
    long id;
    String token;
    Credential credential;

	public TCPReader(InputStream inputStream) {
		super (inputStream);
	}
	private void eraseFields() {
		this.userMail=null;
		this.userPasswd=null;
		this.id=-1;
		this.token=null;
	}

	public void receive() {
		eraseFields();
		type = readInt ();
		switch (type) {
		case 0 : break;
		case Protocol.REQUEST_CONNECT:
        	readUserConnect();
            break;
		case Protocol.REQUEST_GET_ALL_TEXTS:
			getAllTexts();
            break;
		case Protocol.REQUEST_CREATE_USER:
			readUserConnect();
			break;
		case Protocol.REQUEST_CREDENTIAL:
			readUserConnect();
			break;
		}
    }
	public void readcreateUserProcess(){
		credential = new Credential( readString(),readString(),readString());
    }
    public void readcredentialsProcess(){
    	this.token = readString();
        this.id   = readLong();
    }
	public void readUserConnect(){
        this.userMail   = readString();
        this.userPasswd = readString();
	}
	public void getAllTexts(){
		this.token = readString();
        this.id   = readLong();
	}

}
package fr.ensisa.hassenforder.tp.server.network;

import java.util.List;
import java.util.Locale;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    String name;
    String content;
    long id;
    long textId;
    String token;
    Credential credential;
    Role role;
    List<Participant> participants;
    List<OperationIntermediaire> interOperationsToSave;
    List<boolean[]> opToSave ;
	DateFormat formatter;

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
			System.out.println("messageReceived");
			getAllTexts();
            break;
		case Protocol.REQUEST_CREATE_USER:
			readcreateUserProcess();
			break;
		case Protocol.REQUEST_UPDATE_USER:
			updateUser();
			break;
		case Protocol.REQUEST_CREDENTIAL:
			readcredentialsProcess();
			break;
		case Protocol.REQUEST_GET_ALL_USERS:
			readAllUserProcess();
			break;
		case Protocol.REQUEST_PUT_ALL_PARTICIPANTS:
			readTextParticipantProcess();
			break;
		case Protocol.REQUEST_GET_ALL_OPERATIONS:
			readAllTextOperationsProcess();
			break;
		case Protocol.REQUEST_PUT_ALL_OPERATIONS:
			readTextOperationsProcess();
			break;
		case Protocol.REQUEST_GET_TEXT:
			readTextProcess();
			break;
		case Protocol.REQUEST_PUT_TEXT_CONTENT:
			readTextContentProcess();
			break;
		case Protocol.REQUEST_GET_ALL_PARTICIPANTS:
			readAllTextParticipantsProcess();
			break;
		 case Protocol.REQUEST_DELETE_TEXT:
			deleteText();
			break;
		case Protocol.REQUEST_NEW_TEXT:
			newText();
			break;
		}
    }
	  public void deleteText(){
			this.token = readString();
			this.textId   = readLong();
			this.id   = readLong();
		}
		public void newText(){
			this.token = readString();
			this.id   = readLong();
		}
	public void readAllUserProcess(){
        this.token = readString();
    }
    public void readTextParticipantProcess(){
    	this.token=readString();
    	this.textId=readLong();
    	this.id=readLong();
    	this.participants = new ArrayList<Participant>();
    	int size = readInt();
    	for(int i =0 ;i<size; i++){
            long who = readLong();
            Role r = new Role(readInt());
            Participant p = new Participant(who,textId,r);
            participants.add(p);
    	}
    }
	public void readcreateUserProcess(){
		System.out.println("messageReceived");
		this.credential = new Credential( readString(),readString(),readString());
    }
    public void readcredentialsProcess(){
    	this.token = readString();
        this.id   = readLong();
    }
	public void readUserConnect(){
		System.out.println("messageReceived");
        this.userMail   = readString();
        this.userPasswd = readString();
	}
	public void getAllTexts(){
		this.token = readString();
        this.id   = readLong();
	}
	public void updateUser(){
        this.token=readString();
        this.credential = new Credential( readString(),readString(),readString());
	}
	public void readAllTextOperationsProcess(){
		this.token = readString();
		this.textId=readLong();
		this.id    =readLong();

	}
	public void readTextOperationsProcess(){
		this.token = readString();
		this.textId=readLong();
		this.id    =readLong();

		int size = readInt();
        this.opToSave = new ArrayList<boolean[]>();
        this.interOperationsToSave = new ArrayList<OperationIntermediaire>();
		for(int i = 0 ; i<size;i++){
			try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                Date date = sdf.parse(readString());
                boolean[] b = {readBoolean(),readBoolean(),readBoolean()};
                opToSave.add(b);
                interOperationsToSave.add(new OperationIntermediaire(date,readInt(),readString()) );
			}
		    catch (ParseException e) {
                System.out.println("Erreur de parsing : " + e.getMessage());
            }
		}
	}
	public void readTextProcess(){
        this.token = readString();
	    this.textId = readLong();
	    this.id = readLong();
	}
	public void readTextContentProcess(){
		this.token = readString();
		this.textId = readLong();
		this.id = readLong() ;
		this.content = readString();

	}
	public void readAllTextParticipantsProcess(){
		this.token = readString();
		this.textId=readLong();
        this.id=readLong();
	}
	class OperationIntermediaire{
		private Date date;
		private int where;
		private String text;

		public OperationIntermediaire(Date date, int where, String text) {
			this.date = date;
			this.where = where;
			this.text = text;
		}
		public Date getDate(){
			return this.date;
		}
		public int getWhere(){
			return this.where;
		}
	    public String getText(){
			return this.text;
		}

	}
}

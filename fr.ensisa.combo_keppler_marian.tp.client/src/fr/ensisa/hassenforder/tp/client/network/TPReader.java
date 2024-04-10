package fr.ensisa.hassenforder.tp.client.network;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fr.ensisa.hassenforder.network.BasicAbstractReader;
import fr.ensisa.hassenforder.tp.client.model.Credential;
import fr.ensisa.hassenforder.tp.client.model.Participant;
import fr.ensisa.hassenforder.tp.client.model.Role;
import fr.ensisa.hassenforder.tp.client.model.SharedText;
import fr.ensisa.hassenforder.tp.client.model.TextOperation;
import fr.ensisa.hassenforder.tp.client.model.TextOperation.What;
import fr.ensisa.hassenforder.tp.client.model.User;
import fr.ensisa.hassenforder.tp.network.Protocol;

public class TPReader extends BasicAbstractReader {
	long id;
    String name;
    String token ;
    String mail;
    String passwd;
    List<SharedText> output;
    SharedText outputText;
    List<User> users;
    Collection<TextOperation> operations;
    Collection<Participant> participants;
    boolean isOk;
	DateFormat formatter ;

    public TPReader(InputStream inputStream) {
        super(inputStream);
    }

    private void eraseFields() {
//TODO
    }

//TODO

    public void receive() {
        type = readInt();
        eraseFields();
        switch (type) {
        case Protocol.REPLY_KO:
            this.isOk=false;
        	break;
        case Protocol.REPLY_OK:
        	this.isOk=true;
            break;
        case Protocol.REPLY_USER:
        	connectUser();
            break;
        case Protocol.REPLY_ALL_TEXTS:
            allTextsOperations();
            break;
        case Protocol.REPLY_CREDENTIAL:
            giveCredential();
            break;
        case Protocol.REPLY_ALL_USERS:
            replyAllUsers();
            break;
        case Protocol.REPLY_ALL_OPERATIONS:
        	replyAllTextOperationsProcess();
        	break;
        case Protocol.REPLY_TEXT:
        	replyText();
        	break;
        case Protocol.REPLY_ALL_PARTICIPANTS:
        	replyAllParticipants();
        	break;
        }
    }
    public void replyAllParticipants(){
    	participants = new ArrayList<Participant>();
    	int size = readInt();
    	for(int i=0 ; i<size;i++){
        	Participant p = new Participant(readLong(),readString(),new Role(readInt()));
        	participants.add(p);
    	}

    }
    public void replyText(){
    	 long id = readLong();
         String dateS = readString();
         String content = readString();
         int role = readInt();
         String owner = readString();
         try {
             SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
             Date date = sdf.parse(dateS);
             System.out.println("Date convertie : " + date);
             this.outputText = new SharedText(id,date,content,new Role(role),owner);
         } catch (ParseException e) {
             System.out.println("Erreur de parsing : " + e.getMessage());
         }

    }
    public void replyAllTextOperationsProcess(){
    	this.operations  = new ArrayList<TextOperation>();
    	int size = readInt();
    	for(int i = 0;i<size;i++){
    		try{
                Long id = readLong();
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                Date date = sdf.parse(readString());
                String name=readString();
                boolean[] what = {readBoolean(),readBoolean(),readBoolean()};
                int where = readInt();
                String text = readString();

        		What w=What.COMMENT;
				if (what[0] ==true )
					w=What.COMMENT;
				if (what[1] ==true  )
					w=What.INSERT;
				if (what[2] ==true )
					w=What.DELETE;
				TextOperation t = new TextOperation(id,date,name,w,where,text);
                this.operations.add(t);

    		}catch (ParseException e) {
                System.out.println("Erreur de parsing : " + e.getMessage());
            }
    	}

    }

    public void replyAllUsers(){
    	int size = readInt();
    	this.users=new ArrayList<User>();
        for(int i=0;i<size;i++){
            User u = new User(readLong(),readString(),readString());
            users.add(u);
        }
    }
	public void giveCredential(){
        this.id = readLong();
        this.name =readString();
        this.mail = readString();
        this.passwd= readString();
        return;
	}
    public void allTextsOperations(){
    	this.output = new ArrayList<SharedText>();
		int size =readInt();
		for (int i=0;i<size;i++) {
            long id = readLong();
            String dateS = readString();
            String content = readString();
            int role = readInt();
            String owner = readString();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                Date date = sdf.parse(dateS);
                System.out.println("Date convertie : " + date);
                SharedText  text = new SharedText(id,date,content,new Role(role),owner);
    			output.add(text);
            } catch (ParseException e) {
                System.out.println("Erreur de parsing : " + e.getMessage());
            }
		}
    	return;
    }
    public void connectUser(){
        id = readLong();
        name = readString();
        token = readString();
    }

}

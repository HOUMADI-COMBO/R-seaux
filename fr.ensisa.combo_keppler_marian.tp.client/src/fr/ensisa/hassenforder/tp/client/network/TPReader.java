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
    boolean isOK;

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
            this.isOK=false;
        	  break;
        case Protocol.REPLY_OK:
            this.isOK=true;
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


//TODO
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

    	/////////////
    	DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
    	System.out.println("ici---------");
    	//////////////////////////////
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

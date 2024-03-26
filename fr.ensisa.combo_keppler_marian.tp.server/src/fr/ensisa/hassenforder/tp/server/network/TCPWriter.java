package fr.ensisa.hassenforder.tp.server.network;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import fr.ensisa.hassenforder.tp.database.Model;
import fr.ensisa.hassenforder.tp.database.Role;
import fr.ensisa.hassenforder.tp.database.SharedText;
import fr.ensisa.hassenforder.tp.database.User;
import fr.ensisa.hassenforder.tp.network.Protocol;
import fr.ensisa.hassenforder.network.BasicAbstractWriter;

public class TCPWriter extends BasicAbstractWriter {
	private Model model;
    public TCPWriter(OutputStream outputStream) {
        super (outputStream);
		model = new Model();
		model.initialize();
    }

    public void createOK() {
        writeInt(Protocol.REPLY_OK);
    }

    public void createKO() {
        writeInt(Protocol.REPLY_KO);
    }
    public void replyConnect(User user,String token) {
			writeInt(Protocol.REPLY_USER);
	        writeLong(user.getId());
	        writeString(user.getName());
	        writeString(token);
	        return;
    }
    public void sendCredentials( long id, String name, String mail, String passwd){
    	writeInt(Protocol.REPLY_CREDENTIAL);
        writeLong(id);
        writeString(name);
        writeString(mail);
        writeString(passwd);
        return;
    }
    public void writeAllTexts(Collection<SharedTextReply> inputs){
    	System.out.println("error-here");
    	writeInt(Protocol.REPLY_ALL_TEXTS);
    	writeInt(inputs.size());
        for(SharedTextReply t : inputs){
        	writeLong(t.getId());
			writeString( t.getDate().toString() );
			writeString(t.getContent());
			writeInt(t.getRole().asInt());
			writeString(t.getOwner());
        }
    }

}

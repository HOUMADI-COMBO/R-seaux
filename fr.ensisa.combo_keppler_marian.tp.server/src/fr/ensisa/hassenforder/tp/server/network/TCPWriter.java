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
    public void replyConnect(String mail, String passwd) {

    	fr.ensisa.hassenforder.tp.database.User user = model.getUsers().getUserByMail(mail);
		if (user == null){
			writeInt(Protocol.REPLY_KO);
		    return;
		}
		if (user.getPasswd().equals(passwd)) {
			writeInt(Protocol.REPLY_USER);
	        writeLong(user.getId());
	        writeString(user.getName());
	        writeString( model.getTokens().addNewToken(user.getId()));
	        return;
		}else {
			model.getTokens().removeToken(user.getId());
			writeInt(Protocol.REPLY_KO);
			return;
		}
    }
    public void writeAllTexts(Collection<SharedTextReply> inputs){
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
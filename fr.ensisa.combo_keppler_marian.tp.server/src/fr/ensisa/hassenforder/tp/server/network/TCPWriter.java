package fr.ensisa.hassenforder.tp.server.network;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import fr.ensisa.hassenforder.tp.database.Model;
import fr.ensisa.hassenforder.tp.database.Operation;
import fr.ensisa.hassenforder.tp.database.Role;
import fr.ensisa.hassenforder.tp.database.SharedText;
import fr.ensisa.hassenforder.tp.database.User;
import fr.ensisa.hassenforder.tp.database.What;
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
    public void replyAllUsers(Collection<User> users){
    	writeInt(Protocol.REPLY_ALL_USERS);
    	writeInt(users.size());
    	for(User user : users){
    		writeLong(user.getId());
    		writeString(user.getName());
            writeString(user.getMail());
    	}
    }
    public void replyAllTextOperationsProcess(Collection<OperationReply> operations){
    	writeInt(Protocol.REPLY_ALL_OPERATIONS);
        writeInt(operations.size());
        for(OperationReply op : operations){
        	writeLong(op.getId());
        	writeString(op.getDate().toString());
            writeString(op.getName());

            if( op.getWhat() == What.COMMENT )
				writeBoolean(true);
			else
				writeBoolean(false);

			if (op.getWhat() == What.INSERT )
				writeBoolean(true);
			else
				writeBoolean(false);

			if (op.getWhat() == What.DELETE )
				writeBoolean(true);
			else
				writeBoolean(false);

            writeInt(op.getWhere());
            writeString(op.getText());

        }
    }
    public void replyText(SharedTextReply t){
    	writeInt(Protocol.REPLY_TEXT);
        writeLong(t.getId());
		writeString( t.getDate().toString() );
		writeString(t.getContent());
		writeInt(t.getRole().asInt());
		writeString(t.getOwner());
    }
    public void replyAllParticipants(Collection<ParticipantReply> participants){
        writeInt(Protocol.REPLY_ALL_PARTICIPANTS);
        writeInt(participants.size());
        for(ParticipantReply p : participants){
            writeLong(p.getId());
            writeString(p.getName());
            writeInt(p.getRole().asInt());
        }
    }
}

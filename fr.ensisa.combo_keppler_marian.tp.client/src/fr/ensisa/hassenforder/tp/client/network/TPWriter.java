package fr.ensisa.hassenforder.tp.client.network;

import java.io.OutputStream;
import java.util.Collection;

import fr.ensisa.hassenforder.tp.client.model.Credential;
import fr.ensisa.hassenforder.tp.network.Protocol;
import fr.ensisa.hassenforder.network.BasicAbstractWriter;

public class TPWriter extends BasicAbstractWriter {

    public TPWriter(OutputStream outputStream) {
        super(outputStream);
    }
    public void connectionProcess(String  s1,String s2){
        writeInt(Protocol.REQUEST_CONNECT);
    	writeString(s1);
    	writeString(s2);
    }
    public void getAllTxtProcess(String token,long id){
    	writeInt(Protocol.REQUEST_GET_ALL_TEXTS);
    	writeString(token);
    	writeLong(id);
    }
    public void createUserProcess(Credential credential){
    	writeInt(Protocol.REQUEST_CREATE_USER);
        writeString(credential.getName());
        writeString(credential.getMail());
        writeString(credential.getPasswd());
    }
    public void credentialsProcess(String token,long id){
    	writeInt(Protocol.REQUEST_CREDENTIAL);
        writeString(token);
        writeLong(id);
    }
    public void updateUser(String token,Credential credential){
        writeInt(Protocol.REQUEST_UPDATE_USER);
        writeString(token);
        writeString(credential.getName());
        writeString(credential.getMail());
        writeString(credential.getPasswd());
    }
    public void getAllUserProcess(String token){
        writeInt(Protocol.REQUEST_GET_ALL_USERS);
        writeString(token);
    }
    public void saveTextParticipantProcess(String token, long textId, long whoId, Collection<ParticipantOperationRequest> toSave){
    	writeInt(Protocol.REQUEST_PUT_ALL_PARTICIPANTS);
    	writeString(token);
    	writeLong(textId);
    	writeLong(whoId);
    	writeInt(toSave.size());
    	for(ParticipantOperationRequest p : toSave){
            writeLong(p.getWho());
            writeInt(p.getRole().asInt());
    	}
    }
    public void getAllTextOperationsProcess(String token, long textId, long whoId){
        writeInt(Protocol.REQUEST_GET_ALL_OPERATIONS);
        writeString(token);
        writeLong(textId);
        writeLong(whoId);
    }

    public void saveTextOperationsProcess(String token, long textId, long whoId, Collection<TextOperationRequest> toSave){
    	writeInt(Protocol.REQUEST_PUT_ALL_OPERATIONS);
        writeString(token);
        writeLong(textId);
        writeLong(whoId);
        writeInt(toSave.size());
        for(TextOperationRequest t : toSave){
            writeString(t.getDate().toString());

			if( t.getWhat() == TextOperationRequest.What.COMMENT )
				writeBoolean(true);
			else
				writeBoolean(false);

			if (t.getWhat() == TextOperationRequest.What.INSERT )
				writeBoolean(true);
			else
				writeBoolean(false);

			if (t.getWhat() == TextOperationRequest.What.DELETE )
				writeBoolean(true);
			else
				writeBoolean(false);

            writeInt(t.getWhere());
            writeString(t.getText());
        }
    }
    public void  getTextProcess(String token, long textId, long whoId){
        writeInt(Protocol.REQUEST_GET_TEXT);
        writeString(token);
        writeLong(textId);
        writeLong(whoId);
    }
    public void  saveTextContentProcess(String token, long textId, long whoId, String content){
    	writeInt(Protocol.REQUEST_PUT_TEXT_CONTENT);
        writeString(token);
        writeLong(textId);
        writeLong(whoId);
        writeString(content);
    }
    public void getAllTextParticipantsProcess(String token, long textId, long whoId){
        writeInt(Protocol.REQUEST_GET_ALL_PARTICIPANTS);
        writeString(token);
        writeLong(textId);
        writeLong(whoId);
    }
    public void deleteTextProcess(String token,long textId, long id){
      	writeInt(Protocol.REQUEST_DELETE_TEXT);
      	writeString(token);
      	writeLong(textId);
      	writeLong(id);
    }
    public void newTextProcess(String token, long id){
      	writeInt(Protocol.REQUEST_NEW_TEXT);
    	writeString(token);
      	writeLong(id);
    }
}




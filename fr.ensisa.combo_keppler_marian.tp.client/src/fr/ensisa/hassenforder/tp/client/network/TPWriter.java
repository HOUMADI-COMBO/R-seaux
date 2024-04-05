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

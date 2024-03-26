package fr.ensisa.hassenforder.tp.client.network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.ensisa.hassenforder.tp.client.model.Credential;
import fr.ensisa.hassenforder.tp.client.model.Participant;
import fr.ensisa.hassenforder.tp.client.model.SharedText;
import fr.ensisa.hassenforder.tp.client.model.TextOperation;
import fr.ensisa.hassenforder.tp.client.model.User;
import fr.ensisa.hassenforder.tp.network.Protocol;

public class NetworkSession implements ISession {

    private Socket tcp;
    private String host;
    private int port;

    public NetworkSession(String host, int port) {
    	this.host = host;
    	this.port = port;
    }

    @Override
    synchronized public boolean close() {
        try {
            if (tcp != null) {
                tcp.close();
            }
            tcp = null;
        } catch (IOException e) {
        }
        return true;
    }

    @Override
    synchronized public boolean open() {
        this.close();
        try {
            tcp = new Socket(this.host, this.port);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

	@Override
	public User connect(String mail, String passwd) {
		try {
        	TPWriter w = new TPWriter(tcp.getOutputStream());
        	w.connectionProcess(mail,passwd);
            w.send();
            TPReader r = new TPReader(tcp.getInputStream());
            r.receive();
            User currentUser = new User(r.id,r.name,r.token);
    		return currentUser;
        } catch (IOException e) {
    		return null;
        }
	}

	@Override
	public Credential getCredential(String token, long id) {
        try {
        	TPWriter w = new TPWriter(tcp.getOutputStream());
        	w.credentialsProcess(token,id);
            w.send();
            TPReader r = new TPReader(tcp.getInputStream());
            r.receive();
    		return new Credential(r.id,r.name,r.mail,r.passwd);
        } catch (IOException e) {
    		return null;
        }
	}
	@Override
	public Boolean createUser(Credential credential) {
        try {
        	TPWriter w = new TPWriter(tcp.getOutputStream());
        	w.createUserProcess(credential);
            w.send();
            TPReader r = new TPReader(tcp.getInputStream());
            r.receive();
    		return null;
        } catch (IOException e) {
    		return null;
        }
	}

	@Override
	public Boolean updateUser(String token, Credential credential) {
        try {
        	TPWriter w = new TPWriter(tcp.getOutputStream());
            w.updateUser(token,credential);
            w.send();
            TPReader r = new TPReader(tcp.getInputStream());
            r.receive();
    		return null;
        } catch (IOException e) {
    		return null;
        }
	}

	@Override
	public Collection<SharedText> getAllTexts(String token, long id) {
        try {
        	TPWriter w = new TPWriter(tcp.getOutputStream());
            w.getAllTxtProcess(token, id);
            w.send();
            System.out.println("messageSend");
            TPReader r = new TPReader(tcp.getInputStream());
            r.receive();
    		return r.output;
        } catch (IOException e) {
    		return null;
        }
	}

	@Override
	public SharedText getText(String token, long textId, long whoId) {
        try {
        	TPWriter w = new TPWriter(tcp.getOutputStream());
//TODO
            w.send();
            TPReader r = new TPReader(tcp.getInputStream());
            r.receive();
//TODO
    		return null;
        } catch (IOException e) {
    		return null;
        }
	}

	@Override
	public Boolean saveTextContent(String token, long textId, long whoId, String content) {
        try {
        	TPWriter w = new TPWriter(tcp.getOutputStream());
//TODO
            w.send();
            TPReader r = new TPReader(tcp.getInputStream());
            r.receive();
//TODO
    		return null;
        } catch (IOException e) {
    		return null;
        }
	}

	@Override
	public Boolean deleteText(String token, long textId, long whoId) {
        try {
        	TPWriter w = new TPWriter(tcp.getOutputStream());
//TODO
            w.send();
            TPReader r = new TPReader(tcp.getInputStream());
            r.receive();
//TODO
    		return null;
        } catch (IOException e) {
    		return null;
        }
	}

	@Override
	public Boolean newText(String token, long creatorId) {
        try {
        	TPWriter w = new TPWriter(tcp.getOutputStream());
//TODO
            w.send();
            TPReader r = new TPReader(tcp.getInputStream());
            r.receive();
//TODO
    		return null;
        } catch (IOException e) {
    		return null;
        }
	}

	@Override
	public Collection<Participant> getAllTextParticipants(String token, long textId, long whoId) {
        try {
        	TPWriter w = new TPWriter(tcp.getOutputStream());
//TODO
            w.send();
            TPReader r = new TPReader(tcp.getInputStream());
            r.receive();
//TODO
    		return null;
        } catch (IOException e) {
    		return null;
        }
	}

	@Override
	public Collection<User> getAllUsers(String token) {
        try {
        	TPWriter w = new TPWriter(tcp.getOutputStream());
//TODO
            w.send();
            TPReader r = new TPReader(tcp.getInputStream());
            r.receive();
//TODO
    		return null;
        } catch (IOException e) {
    		return null;
        }
	}

	@Override
	public Collection<TextOperation> getAllTextOperations(String token, long textId, long whoId) {
        try {
        	TPWriter w = new TPWriter(tcp.getOutputStream());
//TODO
            w.send();
            TPReader r = new TPReader(tcp.getInputStream());
            r.receive();
//TODO
    		return null;
        } catch (IOException e) {
    		return null;
        }
	}

	@Override
	public Boolean saveTextParticipants(String token, long textId, long whoId, Collection<ParticipantOperationRequest> toSave) {
        try {
        	TPWriter w = new TPWriter(tcp.getOutputStream());
//TODO
            w.send();
            TPReader r = new TPReader(tcp.getInputStream());
            r.receive();
//TODO
    		return null;
        } catch (IOException e) {
    		return null;
        }
	}

	@Override
	public Boolean saveTextOperations(String token, long textId, long whoId, Collection<TextOperationRequest> toSave) {
        try {
        	TPWriter w = new TPWriter(tcp.getOutputStream());
//TODO
            w.send();
            TPReader r = new TPReader(tcp.getInputStream());
            r.receive();
//TODO
    		return null;
        } catch (IOException e) {
    		return null;
        }
	}

}

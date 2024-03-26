package fr.ensisa.hassenforder.tp.server.network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.ensisa.hassenforder.tp.network.Protocol;
import fr.ensisa.hassenforder.tp.database.Model;
import fr.ensisa.hassenforder.tp.database.Operation;
import fr.ensisa.hassenforder.tp.database.Participant;
import fr.ensisa.hassenforder.tp.database.Role;
import fr.ensisa.hassenforder.tp.database.SharedText;
import fr.ensisa.hassenforder.tp.database.User;
import fr.ensisa.hassenforder.tp.database.What;

public class TCPSession extends Thread {

	private Socket connection;
	private Model model;

	public TCPSession(Socket connection, Model model) {
		this.connection = connection;
		this.model = model;
	}
	private SharedTextReply adaptText(SharedText input, long whoId, boolean reduceContent) {
		Collection<Participant> participants = model.getParticipants().getAllParticipants(input.getId());
		if (participants == null) return null;
		long owner = 0;
		Role role = null;
		for (Participant participant : participants) {
			if (participant.getRole().isCreator()) owner = participant.getWho();
			if (participant.getWho() == whoId) role = participant.getRole();
		}
		if (owner == 0) return null;
		if (role == null) return null;
		User user = model.getUsers().getUser(owner);
		if (user == null) return null;
		String content = input.getContent();
		if (reduceContent && content.length() > 20) content = content.substring(0, 18) + " ...";
		return new SharedTextReply(
				input.getId(),
				input.getCreated(),
				content,
				role,
				user.getName()
		);
	}

	private Collection<SharedTextReply> adaptTexts(Collection<SharedText> inputs, long whoId) {
		List<SharedTextReply> output = new ArrayList<>();
		for (SharedText input : inputs) {
			SharedTextReply text = adaptText(input, whoId, true);
			if (text != null) output.add(text);
		}
		return output;
	}

	private ParticipantReply adaptParticipant(Participant participant, String name) {
		return new ParticipantReply(participant.getWho(), name, participant.getRole());
	}

	private Collection<ParticipantReply> adaptParticipants(Collection<Participant> inputs) {
		List<ParticipantReply> output = new ArrayList<>();
		for (Participant input : inputs) {
			User user = model.getUsers().getUser(input.getWho());
			if (user == null) continue;
			ParticipantReply participant = adaptParticipant(input, user.getName());
			if (participant != null) output.add(participant);
		}
		return output;
	}
	private OperationReply adaptOperation(Operation operation, String name) {
		return new OperationReply(
				operation.getId(),
				operation.getDate(),
				name,
				operation.getWhat(),
				operation.getWhere(),
				operation.getText()
		);
	}

	private Collection<OperationReply> adaptOperations(Collection<Operation> inputs) {
		List<OperationReply> output = new ArrayList<>();
		for (Operation input : inputs) {
			User user = model.getUsers().getUser(input.getWho());
			if (user == null) continue;
			OperationReply operation = adaptOperation(input, user.getName());
			if (operation != null) output.add(operation);
		}
		return output;
	}

	public void close () {
		this.interrupt();
		try {
			if (connection != null)
				connection.close();
		} catch (IOException e) {
		}
		connection = null;
	}

//TODO

	public boolean operate() {
		try {
			boolean result = true;
			TCPWriter writer = new TCPWriter (connection.getOutputStream());
			TCPReader reader = new TCPReader (connection.getInputStream());
			reader.receive();
			switch (reader.getType ()) {
			case 0 : result = false; break; // socket closed
			case Protocol.REQUEST_CONNECT:
                writer.replyConnect( reader.userMail , reader.userPasswd);
                writer.send();
				break;
			case Protocol.REQUEST_GET_ALL_TEXTS:

				if (! model.getTokens().isKnown(reader.token))  {
					writer.createKO();    break;   }

				Collection<SharedTextReply> outputs = adaptTexts(model.getAllTexts(reader.id), reader.id);
                writer.writeAllTexts(outputs);
                writer.send();
				break;
			default: result = false; // connection jammed
			}
			if (result) writer.send();
			return result;
		} catch (IOException e) {
			return false;
		}
	}

	public void run() {
		while (true) {
			if (! operate())
				break;
		}
		try {
			if (connection != null) connection.close();
		} catch (IOException e) {
		}
	}

}

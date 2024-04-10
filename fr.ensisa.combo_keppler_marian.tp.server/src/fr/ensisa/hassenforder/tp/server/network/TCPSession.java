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
			case 0 :
				result = false;
			    break; // socket closed

			case Protocol.REQUEST_CONNECT:
				fr.ensisa.hassenforder.tp.database.User user = model.getUsers().getUserByMail(reader.userMail);
				if (user == null){
					writer.createKO();
					break;
				}
		        String token = model.getTokens().addNewToken(user.getId());
				writer.replyConnect( user,token);
				break;
			case Protocol.REQUEST_GET_ALL_TEXTS:
				if (! model.getTokens().isKnown(reader.token))  {
					System.out.println("messageReveived-noToken");
					writer.createKO();
					break;
				}

				Collection<SharedTextReply> outputs = adaptTexts(model.getAllTexts(reader.id), reader.id);
                writer.writeAllTexts(outputs);
				break;
			case Protocol.REQUEST_CREATE_USER:
				if (reader.credential.getName().isEmpty()){
					System.out.println("messageReveived-noToken");
					writer.createKO();
					break;
				}
				fr.ensisa.hassenforder.tp.database.User user1 = model.getUsers().getUserByName(reader.credential.getName());
				if (user1 != null){
					System.out.println("User already exist");
					writer.createKO();
					break;
				}
				model.getUsers().addUser(	new User(	reader.credential.getName(),reader.credential.getMail(),reader.credential.getPasswd()));
				System.out.println("User created !");
				writer.createOK();
				break;
			case Protocol.REQUEST_CREDENTIAL:
				if (! model.getTokens().isKnown(reader.token)){
					writer.createKO();
					break;
				}
				User user2 = model.getUsers().getUser(reader.id);
				if (user2 == null){
					writer.createKO();
					break;
				}
				writer.sendCredentials(  user2.getId(), user2.getName(), user2.getMail(), user2.getPasswd());
				break;
			case Protocol.REQUEST_UPDATE_USER:
				if (! model.getTokens().isKnown(reader.token)){
					writer.createKO();
					break;
				}
				if (reader.credential.getName().isEmpty()){
					writer.createKO();
					break;
				}
				User user3= model.getUsers().getUserByName(reader.credential.getName());
				if (user3 == null){
					writer.createKO();
					break;
				}
				user3.setMail(reader.credential.getMail());
				user3.setPasswd(reader.credential.getPasswd());
				writer.createOK();
				break;
			case Protocol.REQUEST_GET_ALL_USERS:
				if (! model.getTokens().isKnown(reader.token)){
					writer.createKO();
					break;
				}
				Collection<User> users = model.getUsers().getAll();
				writer.replyAllUsers(users);
				break;
			case Protocol.REQUEST_PUT_ALL_PARTICIPANTS:
				if (! model.getTokens().isKnown(reader.token))
					writer.createKO();
				Participant submitter = model.getParticipants().getParticipant(reader.textId, reader.id);
				if (submitter == null)
					writer.createKO();;
				if (! submitter.getRole().isCreator())
					writer.createKO();
				model.getParticipants().update(reader.textId, reader.participants);
				writer.createOK();
				break;
			case Protocol.REQUEST_GET_ALL_OPERATIONS:
				if (! model.getTokens().isKnown(reader.token))
					writer.createKO();
				fr.ensisa.hassenforder.tp.database.Participant participant = model.getParticipants().getParticipant(reader.textId, reader.id);
				if (participant == null)
					writer.createKO();
				if (! participant.getRole().canRead())
					writer.createKO();
				Collection<Operation> operations =model.getOperations().getAllOperations(reader.textId);
				writer.replyAllTextOperationsProcess(adaptOperations(operations));
				break;
			case Protocol.REQUEST_PUT_ALL_OPERATIONS:
				if (! model.getTokens().isKnown(reader.token))
					writer.createKO();
				fr.ensisa.hassenforder.tp.database.Participant submitter2 = model.getParticipants().getParticipant(reader.textId,reader.id);
				if (submitter2 == null)
					writer.createKO();
				int k=0;
				for (boolean[] b : reader.opToSave) {
					What w =What.COMMENT;
					boolean valid = false;
					if (b[0] ==true && submitter2.getRole().canComment()){
						valid = true;
						w=What.COMMENT;
					}

					if (b[1] ==true  && submitter2.getRole().canEdit()){
						valid = true;
						w=What.INSERT;
					}

					if (b[2] ==true  && submitter2.getRole().canEdit()){
						valid = true;
						w=What.DELETE;
					}
					if (! valid) continue;
					model.getOperations().addOperation(
						new fr.ensisa.hassenforder.tp.database.Operation (
								reader.interOperationsToSave.get(k).getDate(),
								reader.id,
								reader.textId,
								What.valueOf(w.name().toUpperCase()),
								reader.interOperationsToSave.get(k).getWhere(),
								reader.interOperationsToSave.get(k).getText()
						)
					);
					k++;
				}
                writer.createOK();
				break;
			case Protocol.REQUEST_PUT_TEXT_CONTENT:
				if (! model.getTokens().isKnown(reader.token))
					writer.createKO();
				fr.ensisa.hassenforder.tp.database.Participant participant3 = model.getParticipants().getParticipant(reader.textId, reader.id);
				if (participant3 == null)
					writer.createKO();
				if (! participant3.getRole().canEdit())
					writer.createKO();
				SharedText text = model.getTexts().getSharedText(reader.textId);
				text.setContent(reader.content);
				writer.createOK();
				break;
			case Protocol.REQUEST_GET_TEXT:
				if (! model.getTokens().isKnown(reader.token))
					writer.createKO();
				Participant participant4 = model.getParticipants().getParticipant(reader.textId, reader.id);
				if (participant4 == null)
					writer.createKO();
				if (! participant4.getRole().canRead())
					writer.createKO();
				adaptText(model.getTexts().getSharedText(reader.textId), reader.id, false);
				SharedTextReply t = adaptText(model.getTexts().getSharedText(reader.textId), reader.id, false);
                writer.replyText(t);
				break;
			case Protocol.REQUEST_GET_ALL_PARTICIPANTS:
				if (! model.getTokens().isKnown(reader.token))
					writer.createKO();
				fr.ensisa.hassenforder.tp.database.Participant participant5 = model.getParticipants().getParticipant(reader.textId, reader.id);
				if (participant5 == null)
					writer.createKO();
				if (! participant5.getRole().isCreator())
					writer.createKO();
				Collection<ParticipantReply> participants = adaptParticipants(model.getParticipants().getAllParticipants(reader.textId));
				writer.replyAllParticipants(participants);
				break;
			case Protocol.REQUEST_DELETE_TEXT:
				if (! model.getTokens().isKnown(reader.token)){
					writer.createKO();
					break;
				}
				fr.ensisa.hassenforder.tp.database.Participant participant6 = model.getParticipants().getParticipant(reader.textId, reader.id);
				if (participant6 == null){
					writer.createKO();
					break;
				}
				if (! participant6.getRole().isCreator()){
					writer.createKO();
					break;
				}
				if (! model.removeText(reader.textId)){
					writer.createKO();
					break;
				}
				writer.createOK();
				break;
			case Protocol.REQUEST_NEW_TEXT:
				if (! model.getTokens().isKnown(reader.token)){
					writer.createKO();
					break;
				}
				fr.ensisa.hassenforder.tp.database.User usertext = model.getUsers().getUser(reader.id);
				if (usertext == null){
					writer.createKO();
					break;
				}
				if (! model.addText(reader.id)){
					writer.createKO();
					break;
				}
				writer.createOK();
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

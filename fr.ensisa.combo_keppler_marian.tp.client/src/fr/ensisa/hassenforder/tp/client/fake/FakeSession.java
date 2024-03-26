package fr.ensisa.hassenforder.tp.client.fake;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.ensisa.hassenforder.tp.client.model.Credential;
import fr.ensisa.hassenforder.tp.client.model.Participant;
import fr.ensisa.hassenforder.tp.client.model.Role;
import fr.ensisa.hassenforder.tp.client.model.SharedText;
import fr.ensisa.hassenforder.tp.client.model.TextOperation;
import fr.ensisa.hassenforder.tp.client.model.User;
import fr.ensisa.hassenforder.tp.client.network.ISession;
import fr.ensisa.hassenforder.tp.client.network.ParticipantOperationRequest;
import fr.ensisa.hassenforder.tp.client.network.TextOperationRequest;
import fr.ensisa.hassenforder.tp.database.Model;
import fr.ensisa.hassenforder.tp.database.What;

public class FakeSession implements ISession {

	private Model model;

	public FakeSession() {
		super();
		model = new Model();
		model.initialize();
	}

	private SharedText adaptText(fr.ensisa.hassenforder.tp.database.SharedText input, long whoId, boolean reduceContent) {
		Collection<fr.ensisa.hassenforder.tp.database.Participant> participants = model.getParticipants().getAllParticipants(input.getId());
		if (participants == null) return null;
		long owner = 0;
		fr.ensisa.hassenforder.tp.database.Role role = null;
		for (fr.ensisa.hassenforder.tp.database.Participant participant : participants) {
			if (participant.getRole().isCreator()) owner = participant.getWho();
			if (participant.getWho() == whoId) role = participant.getRole();
		}
		if (owner == 0) return null;
		if (role == null) return null;
		fr.ensisa.hassenforder.tp.database.User user = model.getUsers().getUser(owner);
		if (user == null) return null;
		String content = input.getContent();
		if (reduceContent && content.length() > 20) content = content.substring(0, 18) + " ...";
		return new SharedText(
				input.getId(),
				input.getCreated(),
				content,
				new Role(role.asInt()),
				user.getName()
		);
	}

	private Collection<SharedText> adaptTexts(Collection<fr.ensisa.hassenforder.tp.database.SharedText> inputs, long whoId) {
		List<SharedText> output = new ArrayList<>();
		for (fr.ensisa.hassenforder.tp.database.SharedText input : inputs) {
			SharedText text = adaptText(input, whoId, true);
			if (text != null) output.add(text);
		}
		return output;
	}

	private Participant adaptParticipant(fr.ensisa.hassenforder.tp.database.Participant input) {
		fr.ensisa.hassenforder.tp.database.User user = model.getUsers().getUser(input.getWho());
		if (user == null) return null;
		return new Participant(input.getWho(), user.getName(), new Role(input.getRole().asInt()) );
	}

	private Collection<Participant> adaptParticipant(Collection<fr.ensisa.hassenforder.tp.database.Participant> inputs) {
		List<Participant> output = new ArrayList<>();
		for (fr.ensisa.hassenforder.tp.database.Participant input : inputs) {
			Participant participant = adaptParticipant(input);
			if (participant != null) output.add(participant);
		}
		return output;
	}

	private User adaptUser(fr.ensisa.hassenforder.tp.database.User input) {
		return new User(input.getId(), input.getName(), null);
	}

	private Collection<User> adaptUser(Collection<fr.ensisa.hassenforder.tp.database.User> inputs) {
		List<User> output = new ArrayList<>();
		for (fr.ensisa.hassenforder.tp.database.User input : inputs) {
			User user = adaptUser(input);
			if (user != null) output.add(user);
		}
		return output;
	}

	private TextOperation adaptOperation (fr.ensisa.hassenforder.tp.database.Operation input) {
		fr.ensisa.hassenforder.tp.database.User user = model.getUsers().getUser(input.getWho());
		if (user == null) return null;
		return new TextOperation(
				input.getId(),
				input.getDate(),
				user.getName(),
				TextOperation.What.valueOf(input.getWhat().name()),
				input.getWhere(),
				input.getText()
		);
	}

	private Collection<TextOperation> adaptOperation(Collection<fr.ensisa.hassenforder.tp.database.Operation> inputs) {
		List<TextOperation> output = new ArrayList<>();
		for (fr.ensisa.hassenforder.tp.database.Operation input : inputs) {
			TextOperation operation = adaptOperation(input);
			if (operation != null) output.add(operation);
		}
		return output;
	}

	@Override
	public boolean open() {
		return true;
	}

	@Override
	public boolean close() {
		return true;
	}

	@Override
	public User connect(String mail, String passwd) {
		fr.ensisa.hassenforder.tp.database.User user = model.getUsers().getUserByMail(mail);
		if (user == null) return null;
		if (user.getPasswd().equals(passwd)) {
			String token = model.getTokens().addNewToken(user.getId());
			return new User (user.getId(), user.getName(), token);
		} else {
			model.getTokens().removeToken(user.getId());
			return null;
		}
	}

	@Override
	public Boolean createUser(Credential credential) {
		if (credential.getName().isEmpty()) return false;
		fr.ensisa.hassenforder.tp.database.User user = model.getUsers().getUserByName(credential.getName());
		if (user != null) return false;
		model.getUsers().addUser(
				new fr.ensisa.hassenforder.tp.database.User(
						credential.getName(),
						credential.getMail(),
						credential.getPasswd()
				)
		);
		return true;
	}

	@Override
	public Credential getCredential(String token, long id) {
		if (! model.getTokens().isKnown(token))
			return null;
		fr.ensisa.hassenforder.tp.database.User user = model.getUsers().getUser(id);
		if (user == null) return null;
		return new Credential (user.getId(), user.getName(), user.getMail(), user.getPasswd());
	}

	@Override
	public Boolean updateUser(String token, Credential credential) {
		if (! model.getTokens().isKnown(token))
			return null;
		if (credential.getName().isEmpty()) return false;
		fr.ensisa.hassenforder.tp.database.User user = model.getUsers().getUserByName(credential.getName());
		if (user == null) return false;
		user.setMail(credential.getMail());
		user.setPasswd(credential.getPasswd());
		return true;
	}

	@Override
	public Collection<SharedText> getAllTexts(String token, long whoId) {
		if (! model.getTokens().isKnown(token))
			return null;
		return adaptTexts(model.getAllTexts(whoId), whoId);
	}

	@Override
	public SharedText getText(String token, long textId, long whoId) {
		if (! model.getTokens().isKnown(token))
			return null;
		fr.ensisa.hassenforder.tp.database.Participant participant = model.getParticipants().getParticipant(textId, whoId);
		if (participant == null) return null;
		if (! participant.getRole().canRead()) return null;
		return adaptText(model.getTexts().getSharedText(textId), whoId, false);
	}

	@Override
	public Boolean saveTextContent(String token, long textId, long whoId, String content) {
		if (! model.getTokens().isKnown(token))
			return null;
		fr.ensisa.hassenforder.tp.database.Participant participant = model.getParticipants().getParticipant(textId, whoId);
		if (participant == null) return Boolean.FALSE;
		if (! participant.getRole().canEdit()) return Boolean.FALSE;
		fr.ensisa.hassenforder.tp.database.SharedText text = model.getTexts().getSharedText(textId);
		text.setContent(content);
		return Boolean.TRUE;
	}

	@Override
	public Boolean deleteText(String token, long textId, long whoId) {
		if (! model.getTokens().isKnown(token))
			return null;
		fr.ensisa.hassenforder.tp.database.Participant participant = model.getParticipants().getParticipant(textId, whoId);
		if (participant == null) return Boolean.FALSE;
		if (! participant.getRole().isCreator()) return Boolean.FALSE;
		if (! model.removeText(textId)) return Boolean.FALSE;
		return Boolean.TRUE;
	}

	@Override
	public Boolean newText(String token, long whoId) {
		if (! model.getTokens().isKnown(token))
			return null;
		fr.ensisa.hassenforder.tp.database.User user = model.getUsers().getUser(whoId);
		if (user == null) return Boolean.FALSE;
		if (! model.addText(whoId)) return Boolean.FALSE;
		return Boolean.TRUE;
	}

	@Override
	public Collection<Participant> getAllTextParticipants(String token, long textId, long whoId) {
		if (! model.getTokens().isKnown(token))
			return null;
		fr.ensisa.hassenforder.tp.database.Participant participant = model.getParticipants().getParticipant(textId, whoId);
		if (participant == null) return null;
		if (! participant.getRole().isCreator()) return null;
		return adaptParticipant(model.getParticipants().getAllParticipants(textId));
	}

	@Override
	public Collection<User> getAllUsers(String token) {
		if (! model.getTokens().isKnown(token))
			return null;
		return adaptUser(model.getUsers().getAll());
	}

	@Override
	public Collection<TextOperation> getAllTextOperations(String token, long textId, long whoId) {
		if (! model.getTokens().isKnown(token))
			return null;
		fr.ensisa.hassenforder.tp.database.Participant participant = model.getParticipants().getParticipant(textId, whoId);
		if (participant == null) return null;
		if (! participant.getRole().canRead()) return null;
		return adaptOperation(model.getOperations().getAllOperations(textId));
	}

	@Override
	public Boolean saveTextParticipants(String token, long textId, long whoId, Collection<ParticipantOperationRequest> toSave) {
		if (! model.getTokens().isKnown(token))
			return null;
		fr.ensisa.hassenforder.tp.database.Participant submitter = model.getParticipants().getParticipant(textId, whoId);
		if (submitter == null) return Boolean.FALSE;
		if (! submitter.getRole().isCreator()) return Boolean.FALSE;
		Collection<fr.ensisa.hassenforder.tp.database.Participant> incoming = new ArrayList<>();
		for (ParticipantOperationRequest participant : toSave) {
			incoming.add(
				new fr.ensisa.hassenforder.tp.database.Participant (
						participant.getWho(),
						textId,
						new fr.ensisa.hassenforder.tp.database.Role(participant.getRole().asInt())
				)
			);
		}
		model.getParticipants().update(textId, incoming);
		return Boolean.TRUE;
	}

	@Override
	public Boolean saveTextOperations(String token, long textId, long whoId, Collection<TextOperationRequest> toSave) {
		if (! model.getTokens().isKnown(token))
			return null;
		fr.ensisa.hassenforder.tp.database.Participant submitter = model.getParticipants().getParticipant(textId, whoId);
		if (submitter == null) return Boolean.FALSE;
		for (TextOperationRequest operation : toSave) {
			boolean valid = false;
			if (operation.getWhat() == TextOperationRequest.What.COMMENT && submitter.getRole().canComment())
				valid = true;
			if (operation.getWhat() == TextOperationRequest.What.INSERT && submitter.getRole().canEdit())
				valid = true;
			if (operation.getWhat() == TextOperationRequest.What.DELETE && submitter.getRole().canEdit())
				valid = true;
			if (! valid) continue;
			model.getOperations().addOperation(
				new fr.ensisa.hassenforder.tp.database.Operation (
						operation.getDate(),
						whoId,
						textId,
						What.valueOf(operation.getWhat().name().toUpperCase()),
						operation.getWhere(),
						operation.getText()
				)
			);
		}
		return Boolean.TRUE;
	}

}

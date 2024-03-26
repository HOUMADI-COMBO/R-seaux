package fr.ensisa.hassenforder.tp.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Model {

	private TokenTable tokens = new TokenTable();
	private UserTable users = new UserTable();
	private TextTable texts = new TextTable();
	private ParticipantTable participants = new ParticipantTable();
	private OperationTable operations = new OperationTable();

	public UserTable getUsers() {
		return users;
	}

	public TextTable getTexts() {
		return texts;
	}

	public ParticipantTable getParticipants() {
		return participants;
	}

	public OperationTable getOperations() {
		return operations;
	}

	public TokenTable getTokens() {
		return tokens;
	}

	public void initialize () {
		long mimi = users.addUser (new User ("mimi", "i@m", "iii"));
		long momo = users.addUser (new User ("momo", "o@m", "ooo"));
		long mama = users.addUser (new User ("mama", "a@m", "aaa"));
		long mumu = users.addUser (new User ("mumu", "u@m", "uuu"));

		long text1 = texts.addSharedText (new SharedText (new Date(), "short text"));
		long text2 = texts.addSharedText (new SharedText (new Date(),
				"A very long text to check the cut at 20 characters. \n"
				+"A very long text to check the cut at 20 characters. \n"
				+"A very long text to check the cut at 20 characters"
		));
		long text3 = texts.addSharedText (new SharedText (new Date(), "medium text\non two lines"));
		long text4 = texts.addSharedText (new SharedText (new Date(), "nothing else"));

		participants.addParticipant(new Participant(mimi, text1, new Role(Role.ALL)));
		participants.addParticipant(new Participant(mumu, text1, new Role(Role.READER|Role.EDITER)));

		participants.addParticipant(new Participant(mimi, text2, new Role(Role.ALL)));
		participants.addParticipant(new Participant(mumu, text2, new Role(Role.READER|Role.COMMENTER)));
		participants.addParticipant(new Participant(mama, text2, new Role(Role.COMMENTER)));

		participants.addParticipant(new Participant(momo, text3, new Role(Role.ALL)));
		participants.addParticipant(new Participant(mimi, text3, new Role(Role.READER|Role.EDITER)));
		participants.addParticipant(new Participant(mama, text3, new Role(Role.EDITER)));

		participants.addParticipant(new Participant(mama, text4, new Role(Role.ALL)));

		operations.addOperation(new Operation(new Date(), mimi, text1, What.COMMENT, 0, "first comment"));
		operations.addOperation(new Operation(new Date(), mimi, text1, What.DELETE, 10, "typo"));
		operations.addOperation(new Operation(new Date(), mimi, text1, What.INSERT, 21, "well done"));

		operations.addOperation(new Operation(new Date(), mimi, text4, What.COMMENT, 0, "first comment"));
		operations.addOperation(new Operation(new Date(), momo, text4, What.COMMENT, 0, "second comment"));
		operations.addOperation(new Operation(new Date(), mama, text4, What.COMMENT, 0, "third comment"));
		operations.addOperation(new Operation(new Date(), mumu, text4, What.COMMENT, 0, "forth comment"));
	}

	public Collection<SharedText> getAllTexts(long whoId) {
		List<SharedText> result = new ArrayList<>();
		for (SharedText st : getTexts().getAll()) {
			Participant participant = getParticipants().getParticipant(st.getId(), whoId);
			if (participant == null) continue;
			if (! participant.getRole().canRead()) continue;
			result.add(st);
		}
		return result;
	}

	public boolean removeText(long textId) {
		for (Operation o : operations.getAllOperations(textId)) {
			operations.remove(o);
		}
		for (Participant p : participants.getAllParticipants(textId)) {
			participants.remove(p);
		}
		texts.remove(textId);
		return true;
	}

	public boolean addText(long creatorId) {
		SharedText text = new SharedText(new Date(), "");
		long textId = texts.addSharedText(text);
		participants.addParticipant(new Participant(creatorId, textId, new Role(Role.ALL)));
		return true;
	}

}

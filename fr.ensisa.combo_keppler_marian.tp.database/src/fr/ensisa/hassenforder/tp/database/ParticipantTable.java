package fr.ensisa.hassenforder.tp.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ParticipantTable {

	private static long LAST_ID = 1000;

	private Map<Long, Participant> participants = null;

	private Map<Long, Participant> getParticipants () {
		if (participants == null) {
			participants = new TreeMap<>();
		}
		return participants;
	}

	public long addParticipant (Participant participant) {
		participant.setId (++LAST_ID);
		getParticipants ().put(participant.getId(), participant);
		return participant.getId();
	}

	public Participant getParticipant (long textId, long whoId) {
		for (Participant participant : getParticipants().values()) {
			if (participant.getOn() != textId) continue;
			if (participant.getWho() != whoId) continue;
			return participant;
		}
		return null;
	}

	public Participant getParticipant (long id) {
		return getParticipants().get(id);
	}

	public Collection<Participant> getAllParticipants(long textId) {
		List<Participant> participants = new ArrayList<>();
		for (Participant participant : getParticipants().values()) {
			if (participant.getOn() != textId) continue;
			participants.add(participant);
		}
		return participants;
	}

	public void remove(Participant participant) {
		getParticipants().remove(participant.getId());
	}

	public void update(long textId, Collection<Participant> incoming) {
		Differ<Participant, Participant, Long> differ = new Differ<>();
		differ.compute (getAllParticipants(textId), incoming, new Differ.Helper<Participant, Participant, Long> () {

			@Override
			public Long getXKey(Participant left) {
				return left.getWho();
			}

			@Override
			public Long getYKey(Participant right) {
				return right.getWho();
			}

			@Override
			public boolean same(Participant left, Participant right) {
				if (left.getWho() != right.getWho()) return false;
				if (left.getRole() != right.getRole()) return false;
				return true;
			}

		});
		for (Participant participant : differ.getToDelete()) {
			remove(participant);
		}
		for (Participant participant : differ.getToAdd()) {
			participant.setOn(textId);
			addParticipant(participant);
		}
		for (Pair<Participant, Participant> participant : differ.getToUpdate()) {
			participant.getT().setRole(participant.getU().getRole());
		}
	}

}

package fr.ensisa.hassenforder.tp.database;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class TextTable {

	private static long LAST_ID = 0;

	private Map<Long, SharedText> texts = null;

	private Map<Long, SharedText> getSharedTexts () {
		if (texts == null) {
			texts = new TreeMap<>();
		}
		return texts;
	}

	public long addSharedText (SharedText text) {
		text.setId (++LAST_ID);
		getSharedTexts ().put(text.getId(), text);
		return text.getId();
	}

	public SharedText getSharedText (long id) {
		return getSharedTexts().get(id);
	}

	public Collection<SharedText> getAll() {
		return getSharedTexts().values();
	}

	public void remove (long textId) {
		getSharedTexts().remove(textId);
	}

}

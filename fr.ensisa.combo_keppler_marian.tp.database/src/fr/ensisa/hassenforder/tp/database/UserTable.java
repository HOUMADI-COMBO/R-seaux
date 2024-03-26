package fr.ensisa.hassenforder.tp.database;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class UserTable {

	private static long LAST_ID = 100;

	private Map<Long, User> users = null;

	private Map<Long, User> getUsers () {
		if (users == null) {
			users = new TreeMap<>();
		}
		return users;
	}

	public long addUser (User user) {
		user.setId (++LAST_ID);
		getUsers ().put(user.getId(), user);
		return user.getId();
	}

	public long replaceUser (long id, User user) {
		user.setId (id);
		getUsers ().put(user.getId(), user);
		return user.getId();
	}

	public User getUser (long id) {
		return getUsers().get(id);
	}

	public User getUserByName(String name) {
		for (User user : getUsers().values()) {
			if (user.getName().equals(name))
				return user;
		}
		return null;
	}

	public User getUserByMail(String mail) {
		for (User user : getUsers().values()) {
			if (user.getMail().equals(mail))
				return user;
		}
		return null;
	}

	public Collection<User> getAll() {
		return getUsers().values();
	}

}

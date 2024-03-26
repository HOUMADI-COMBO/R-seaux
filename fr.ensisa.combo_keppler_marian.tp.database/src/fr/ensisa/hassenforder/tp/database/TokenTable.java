package fr.ensisa.hassenforder.tp.database;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class TokenTable {

	private Map<Long, String> idTokens = new TreeMap<>();
	private Map<String, Long> tokenIds = new TreeMap<>();

	static public String newToken () {
		Random rnd = new Random();
		long value = rnd.nextInt(9000) + 1000;
		return ""+value;
	}

	public void addToken (long id, String token) {
		idTokens.put(id, token);
		tokenIds.put(token, id);
	}

	public String addNewToken (long id) {
		String value = newToken ();
		addToken (id, value);
		return value;
	}

	public void removeToken (long id) {
		String token = idTokens.remove(id);
		if (token != null) {
			tokenIds.remove(token);
		}
	}

	public String getToken (long id) {
		return idTokens.get(id);
	}

	public long getId (String token) {
		if (! tokenIds.containsKey(token))
			return 0;
		return tokenIds.get(token);
	}

	public boolean isKnown(String token) {
		return tokenIds.containsKey(token);
	}

	public boolean isValid(String token, long id) {
		if (! tokenIds.containsKey(token)) return false;
		if (tokenIds.get(token) != id) return false;
		if (! idTokens.get(id).equals(token)) return false;
		return true;
	}

}

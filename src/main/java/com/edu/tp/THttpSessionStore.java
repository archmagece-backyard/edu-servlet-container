package com.edu.tp;

import java.util.HashMap;
import java.util.Map;

public class THttpSessionStore {

	private static THttpSessionStore INSTANCE = new THttpSessionStore();

	private Map<Long, THttpSession> sessions = new HashMap<>();

	private THttpSessionStore() {
	}

	public synchronized static void set(long key) {
		if (!INSTANCE.sessions.containsKey(key)) {
			INSTANCE.sessions.put(key, new THttpSession());
		}
	}

	public static THttpSession get(long key) {
		return INSTANCE.sessions.get(key);

	}
}

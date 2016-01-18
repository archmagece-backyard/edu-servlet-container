package com.edu.tp;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;

public class Storage {

	private static Storage INSTANCE = new Storage();
	private Map<String, HttpServlet> servlets = new HashMap<>();

	private Storage() {
	}

	public synchronized static HttpServlet fetch(String uri) {

		HttpServlet result = INSTANCE.servlets.get(uri);
		if (result == null) {

			try {
				result = (HttpServlet) Class.forName(uri).newInstance();
				INSTANCE.servlets.put(uri, result);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			}

		}
		return result;
	}
}

package br.ufrn.imd.primavera.remoting.entities;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpHeaders {
	private final Map<String, String> headers = new HashMap<>();

	public void add(String key, String value) {
		headers.put(key, value);
	}

	public String get(String key) {
		return headers.get(key);
	}

	public Map<String, String> toMap() {
		return Collections.unmodifiableMap(headers);
	}
}
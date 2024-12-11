package br.ufrn.imd.primavera.remoting.enums;

public enum Verb {
	GET("GET"),
	HEAD("HEAD"),
	POST("POST"),
	PUT("PUT"),
	DELETE("DELETE"),
	CONNECT("CONNECT"),
	OPTIONS("OPTIONS"),
	TRACE("TRACE"),
	PATCH("PATCH");

	private String verb;

	private Verb(String verb) {
		this.verb = verb;
	}

	public String getMethod() {
		return verb;
	}

	public Verb from(String method) {
		for (Verb m : Verb.values()) {
			if (m.getMethod().equals(method)) {
				return m;
			}
		}

		return null;
	}
}

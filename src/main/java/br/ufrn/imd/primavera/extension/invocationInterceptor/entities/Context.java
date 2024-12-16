package br.ufrn.imd.primavera.extension.invocationInterceptor.entities;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Context {
	private Map<String, Object> data;

	public Context() {
		this.data = Collections.synchronizedMap(new HashMap<>());
	}

	public Map<String, Object> getData() {
		return data;
	}
}
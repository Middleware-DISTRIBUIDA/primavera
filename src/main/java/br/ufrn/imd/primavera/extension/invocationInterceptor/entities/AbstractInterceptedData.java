package br.ufrn.imd.primavera.extension.invocationInterceptor.entities;

import java.util.Map;

public abstract class AbstractInterceptedData {
	private String body;
	private Map<String, String> headers;
	private String path;
	private volatile Context context;

	public AbstractInterceptedData() {
	}

	public AbstractInterceptedData(String body, Map<String, String> headers, String path, Context context) {
		this.body = body;
		this.headers = headers;
		this.path = path;
		this.context = context;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Context getContext() {
		return context;
	}
}

package br.ufrn.imd.primavera.remoting.handlers.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class Request {
	private String method;
	private String baseUrl;
	private String path;
	private Map<String, String> queryParams;
	private Map<String, String> headers;
	private String body;

	public Request(String method, String baseUrl, String path) {
		this.method = method;
		this.baseUrl = baseUrl;
		this.path = path;
		this.queryParams = new HashMap<>();
		this.headers = new HashMap<>();
	}

	public Request addQueryParam(String key, String value) {
		queryParams.put(key, value);
		return this;
	}

	public Request addHeader(String key, String value) {
		headers.put(key, value);
		return this;
	}

	public Request setBody(String body) {
		this.body = body;
		return this;
	}

	public URI buildUri() throws URISyntaxException {
		StringBuilder uriBuilder = new StringBuilder();
		uriBuilder.append(baseUrl);
		if (!baseUrl.endsWith("/") && !path.startsWith("/")) {
			uriBuilder.append("/");
		}
		uriBuilder.append(path);

		if (!queryParams.isEmpty()) {
			uriBuilder.append("?");
			queryParams.forEach((k, v) -> uriBuilder.append(k).append("=").append(v).append("&"));
			uriBuilder.setLength(uriBuilder.length() - 1);
		}

		return new URI(uriBuilder.toString());
	}

	public String getMethod() {
		return method;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public String getBody() {
		return body;
	}
}

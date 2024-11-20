package br.ufrn.imd.primavera.remoting.entities;

import br.ufrn.imd.primavera.remoting.enums.HTTPStatus;

public class ResponseWrapper<T> {
	private final T body;
	private final HTTPStatus status;
	private final HttpHeaders headers;

	private ResponseWrapper(T body, HTTPStatus status, HttpHeaders headers) {
		this.body = body;
		this.status = status;
		this.headers = headers;
	}

	public T getBody() {
		return body;
	}

	public HTTPStatus getStatus() {
		return status;
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public static <T> ResponseWrapper<T> ok(T body) {
		return new ResponseWrapper<>(body, HTTPStatus.OK, new HttpHeaders());
	}

	public static <T> ResponseWrapper<T> status(HTTPStatus status, T body) {
		return new ResponseWrapper<>(body, status, new HttpHeaders());
	}

	public static <T> ResponseWrapper<T> withHeaders(T body, HTTPStatus status, HttpHeaders headers) {
		return new ResponseWrapper<>(body, status, headers);
	}
}
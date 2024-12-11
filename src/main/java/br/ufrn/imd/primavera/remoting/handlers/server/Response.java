package br.ufrn.imd.primavera.remoting.handlers.server;

import br.ufrn.imd.primavera.remoting.enums.HTTPStatus;

public class Response<T> {

	private HTTPStatus status;
	private String message;
	private T entity;

	public Response() {
	}

	public Response(HTTPStatus status, String message, T entity) {
		this.status = status;
		this.message = message;
		this.entity = entity;
	}

	public Response(HTTPStatus status, T entity) {
		this.status = status;
		this.entity = entity;
	}

	public Response(HTTPStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	public HTTPStatus getStatus() {
		return status;
	}

	public void setCode(HTTPStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}
}

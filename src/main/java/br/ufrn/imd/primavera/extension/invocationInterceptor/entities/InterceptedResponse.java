package br.ufrn.imd.primavera.extension.invocationInterceptor.entities;

import java.util.Map;

import br.ufrn.imd.primavera.remoting.enums.HTTPStatus;

public class InterceptedResponse extends AbstractInterceptedData {

	private HTTPStatus status;

	public InterceptedResponse() {
	}

	public InterceptedResponse(String body, Map<String, String> headers, String path, HTTPStatus status, Context context) {
		super(body, headers, path, context);
		this.status = status;
	}

	public HTTPStatus getStatus() {
		return status;
	}

	public void setStatus(HTTPStatus status) {
		this.status = status;
	}
}

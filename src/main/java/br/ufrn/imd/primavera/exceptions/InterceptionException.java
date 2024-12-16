package br.ufrn.imd.primavera.exceptions;

import br.ufrn.imd.primavera.remoting.entities.ApiErrorResponseRecord;
import br.ufrn.imd.primavera.remoting.entities.ResponseWrapper;

public class InterceptionException extends PrimaveraException {
	private static final long serialVersionUID = -2846067300370708276L;

	private ResponseWrapper<ApiErrorResponseRecord> response;

	public InterceptionException(String message, ResponseWrapper<ApiErrorResponseRecord> response) {
		super(message);
		this.response = response;
	}

	public ResponseWrapper<ApiErrorResponseRecord> getResponse() {
		return response;
	}
}

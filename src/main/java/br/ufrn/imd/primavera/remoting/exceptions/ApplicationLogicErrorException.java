package br.ufrn.imd.primavera.remoting.exceptions;

public class ApplicationLogicErrorException extends RemotingErrorException {
	private static final long serialVersionUID = -2296923492773609628L;

	public ApplicationLogicErrorException(String message) {
		super(message);
	}

	public ApplicationLogicErrorException(String message, Throwable cause) {
		super(message, cause);
	}
}
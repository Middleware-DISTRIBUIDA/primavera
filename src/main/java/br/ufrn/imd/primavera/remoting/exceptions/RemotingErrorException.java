package br.ufrn.imd.primavera.remoting.exceptions;

public class RemotingErrorException extends Exception {
	private static final long serialVersionUID = -3492266834573012072L;

	public RemotingErrorException(String message) {
		super(message);
	}

	public RemotingErrorException(String message, Throwable cause) {
		super(message, cause);
	}
}
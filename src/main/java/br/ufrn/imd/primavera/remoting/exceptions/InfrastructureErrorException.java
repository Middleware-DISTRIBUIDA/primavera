package br.ufrn.imd.primavera.remoting.exceptions;

public class InfrastructureErrorException extends RemotingErrorException {
	private static final long serialVersionUID = 3690566839345453899L;

	public InfrastructureErrorException(String message) {
		super(message);
	}

	public InfrastructureErrorException(String message, Throwable cause) {
		super(message, cause);
	}
}
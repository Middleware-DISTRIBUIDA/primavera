package br.ufrn.imd.primavera.marshaller.exceptions;

public class SerializationException extends Exception {
	private static final long serialVersionUID = 2610824928390752051L;

	public SerializationException(String message, Throwable cause) {
		super(message, cause);
	}
}
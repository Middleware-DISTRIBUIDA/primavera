package br.ufrn.imd.primavera.remoting.marshaller.interfaces;

import java.io.IOException;

import br.ufrn.imd.primavera.remoting.marshaller.exceptions.SerializationException;

public interface Marshaller<T> {
	T marshal(Object obj) throws SerializationException;

	<R> R unmarshal(T data, Class<R> targetClass) throws IOException, SerializationException;
}

package br.ufrn.imd.primavera.marshaller.interfaces;

import java.io.IOException;

import br.ufrn.imd.primavera.marshaller.exceptions.SerializationException;

public interface Marshaller<T> {
	T marshal(Object obj) throws SerializationException;

	<R> R unmarshal(T data, Class<R> targetClass) throws IOException, SerializationException;
}

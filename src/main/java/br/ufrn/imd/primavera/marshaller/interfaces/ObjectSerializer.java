package br.ufrn.imd.primavera.marshaller.interfaces;

import br.ufrn.imd.primavera.marshaller.exceptions.SerializationException;

public interface ObjectSerializer<T> {
	byte[] serialize(T obj) throws SerializationException;

	T deserialize(byte[] data) throws SerializationException;
}
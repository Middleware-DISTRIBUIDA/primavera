package br.ufrn.imd.primavera.remoting.marshaller.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import br.ufrn.imd.primavera.remoting.marshaller.exceptions.SerializationException;
import br.ufrn.imd.primavera.remoting.marshaller.interfaces.Marshaller;

public class BinaryMarshaller implements Marshaller<byte[]> {
	@Override
	public byte[] marshal(Object obj) throws SerializationException {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bos)) {
			oos.writeObject(obj);
			return bos.toByteArray();
		} catch (IOException e) {
			throw new SerializationException("Failed to serialize object", e);
		}
	}

	@Override
	public <T> T unmarshal(byte[] data, Class<T> targetClass) throws SerializationException {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
				ObjectInputStream ois = new ObjectInputStream(bis)) {
			return targetClass.cast(ois.readObject());
		} catch (IOException | ClassNotFoundException e) {
			throw new SerializationException("Failed to deserialize object", e);
		}
	}
}

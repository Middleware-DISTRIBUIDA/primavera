package br.ufrn.imd.primavera.marshaller;

import br.ufrn.imd.primavera.marshaller.interfaces.Marshaller;

public class MarshallerFactory {

	public static Marshaller<?> getMarshaller(MarshallerType type) {
		return type.createMarshaller();
	}
}
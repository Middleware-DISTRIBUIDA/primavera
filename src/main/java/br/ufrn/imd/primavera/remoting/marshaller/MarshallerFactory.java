package br.ufrn.imd.primavera.remoting.marshaller;

import br.ufrn.imd.primavera.remoting.marshaller.interfaces.Marshaller;

public class MarshallerFactory {

	public static Marshaller<?> getMarshaller(MarshallerType type) {
		return type.createMarshaller();
	}
}
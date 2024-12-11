package br.ufrn.imd.primavera.remoting.marshaller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import br.ufrn.imd.primavera.remoting.marshaller.interfaces.Marshaller;

public class MarshallerFactory {

	private static final Map<MarshallerType, Marshaller<?>> marshallerCache = new ConcurrentHashMap<>();

	public static Marshaller<?> getMarshaller(MarshallerType type) {
		return marshallerCache.computeIfAbsent(type, MarshallerType::createMarshaller);
	}
}
package br.ufrn.imd.primavera.remoting.identification;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.imd.primavera.remoting.entities.AbsoluteObjectReference;
import br.ufrn.imd.primavera.remoting.entities.ObjectID;

public class RemoteObjectRegistry {
	private static RemoteObjectRegistry instance;
	private final ObjectIDRegistry objectIDRegistry;
	private final List<AbsoluteObjectReference> aors;

	public RemoteObjectRegistry() {
		this.objectIDRegistry = ObjectIDRegistry.getInstance();
		this.aors = new ArrayList<>();
	}

	public static synchronized RemoteObjectRegistry getInstance() {
		if (instance == null) {
			instance = new RemoteObjectRegistry();
		}
		return instance;
	}

	public AbsoluteObjectReference register(Class<?> clazz, Object newInstance, String host, int port) {
		objectIDRegistry.addId(clazz, newInstance);
		ObjectID objectId = new ObjectID(clazz);

		for (AbsoluteObjectReference aor : aors) {
			if (aor.getObjectId().equals(objectId) && aor.getHost().equals(host) && aor.getPort() == port) {
				System.out.println("AOR já registrado: " + aor);
				return aor;
			}
		}

		AbsoluteObjectReference newAOR = new AbsoluteObjectReference(host, port, objectId);
		aors.add(newAOR);
		System.out.println("Registrado: " + objectId);

		return newAOR;
	}

	public AbsoluteObjectReference getOrRegisterAOR(Class<?> clazz, Object newInstance, String host, int port) {
		ObjectID objectId = new ObjectID(clazz);

		for (AbsoluteObjectReference aor : aors) {
			if (aor.getObjectId().equals(objectId) && aor.getHost().equals(host) && aor.getPort() == port) {
				System.out.println("AOR já registrado: " + aor);
				return aor;
			}
		}
		AbsoluteObjectReference aor = register(clazz, newInstance, host, port);
		aors.add(aor);
		return aor;
	}

}

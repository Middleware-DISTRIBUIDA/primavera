package br.ufrn.imd.primavera.remoting.identification;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import br.ufrn.imd.primavera.remoting.entities.ObjectID;

public class ObjectIDRegistry {
	private static ObjectIDRegistry instance;
	private final Map<ObjectID, Object> instancesId;
	private final Set<ObjectID> registeredIds;

	private ObjectIDRegistry() {
		this.instancesId = new HashMap<>();
		this.registeredIds = new HashSet<>();
	}

	public static synchronized ObjectIDRegistry getInstance() {
		if (instance == null) {
			instance = new ObjectIDRegistry();
		}
		return instance;
	}

	public <T> void addId(Class<T> clazz, Object id) {
		ObjectID objectId = new ObjectID(clazz);
		if (instancesId.containsKey(objectId)) {
			throw new IllegalArgumentException("A classe já possui um ID associado.");
		}
		instancesId.put(objectId, id);
		registeredIds.add(objectId);
	}

	public <T> Object getId(Class<T> clazz) {
		return instancesId.get(new ObjectID(clazz));
	}

	public <T> boolean containsId(Class<T> clazz) {
		return instancesId.containsKey(new ObjectID(clazz));
	}

	public boolean containsObjectID(ObjectID objectId) {
		return registeredIds.contains(objectId);
	}

	public <T> boolean containsObjectID(Class<T> clazz) {
		return registeredIds.contains(new ObjectID(clazz));
	}

	public <T> ObjectID getOrCreateObjectID(Class<T> clazz) {
		ObjectID objectId = new ObjectID(clazz);
		if (!registeredIds.contains(objectId)) {
			registeredIds.add(objectId);
		}
		return objectId;
	}
}

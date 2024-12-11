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
        registeredIds.add(objectId); // Adiciona à lista de IDs registrados
    }

    public <T> Object getId(Class<T> clazz) {
        return instancesId.get(new ObjectID(clazz)); // Retorna o ID diretamente do Map
    }

    public <T> boolean containsId(Class<T> clazz) {
        return instancesId.containsKey(new ObjectID(clazz)); // Verifica se o Map contém a classe
    }

    public boolean containsObjectID(ObjectID objectId) {
        return registeredIds.contains(objectId); // Verifica no conjunto de IDs registrados
    }

    public <T> boolean containsObjectID(Class<T> clazz) {
        return registeredIds.contains(new ObjectID(clazz)); // Verifica se o ObjectID está registrado
    }
}

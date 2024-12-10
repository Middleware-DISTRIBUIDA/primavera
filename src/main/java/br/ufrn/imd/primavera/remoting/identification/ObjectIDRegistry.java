package br.ufrn.imd.primavera.remoting.identification;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import br.ufrn.imd.primavera.remoting.entities.ObjectID;

public class ObjectIDRegistry {
    private static ObjectIDRegistry instance;
    private final Map<ObjectID, Object> instancesId; // Map para armazenar os IDs associados às classes
    private final Set<ObjectID> registeredIds; // Conjunto de IDs existentes

    // Construtor privado para evitar instância externa
    private ObjectIDRegistry() {
        this.instancesId = new HashMap<>();
        this.registeredIds = new HashSet<>();
    }

    // Método para obter a instância única do singleton
    public static synchronized ObjectIDRegistry getInstance() {
        if (instance == null) {
            instance = new ObjectIDRegistry();
        }
        return instance;
    }

    // Método para adicionar um novo ObjectID
    public <T> void addId(Class<T> clazz, Object id) {
        ObjectID objectId = new ObjectID(clazz);
        if (instancesId.containsKey(objectId)) {
            throw new IllegalArgumentException("A classe já possui um ID associado.");
        }
        instancesId.put(objectId, id);
        registeredIds.add(objectId); // Adiciona à lista de IDs registrados
    }

    // Método para recuperar o ID associado a uma classe
    public <T> Object getId(Class<T> clazz) {
        return instancesId.get(new ObjectID(clazz)); // Retorna o ID diretamente do Map
    }

    // Método para verificar se uma classe já possui um ID associado
    public <T> boolean containsId(Class<T> clazz) {
        return instancesId.containsKey(new ObjectID(clazz)); // Verifica se o Map contém a classe
    }

    // Método para verificar se um ObjectID já existe
    public boolean containsObjectID(ObjectID objectId) {
        return registeredIds.contains(objectId); // Verifica no conjunto de IDs registrados
    }

    // Método para verificar se um ObjectID existe com base em uma classe
    public <T> boolean containsObjectID(Class<T> clazz) {
        return registeredIds.contains(new ObjectID(clazz)); // Verifica se o ObjectID está registrado
    }
}

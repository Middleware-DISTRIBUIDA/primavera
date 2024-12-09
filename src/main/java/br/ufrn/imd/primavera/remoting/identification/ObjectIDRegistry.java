package br.ufrn.imd.primavera.remoting.identification;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.imd.primavera.remoting.entities.ObjectID;

public class ObjectIDRegistry {
    private static ObjectIDRegistry instance; // Instância única do singleton
    private final List<ObjectID> instancesID = new ArrayList<>();

    // Construtor privado para evitar instância externa
    private ObjectIDRegistry() {
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
        for (ObjectID objectId : instancesID) {
            if (objectId.getClazz().equals(clazz)) {
                throw new IllegalArgumentException("A classe já possui um ID associado.");
            }
        }
        instancesID.add(new ObjectID(clazz, id));
    }

    // Método para recuperar o ID associado a uma classe
    public <T> Object getId(Class<T> clazz) {
        for (ObjectID objectId : instancesID) {
            if (objectId.getClazz().equals(clazz)) {
                return objectId.getInstance();
            }
        }
        return null; // Retorna null se não encontrado
    }

    // Método para verificar se uma classe já possui um ID associado
    public <T> boolean containsId(Class<T> clazz) {
        for (ObjectID objectId : instancesID) {
            if (objectId.getClazz().equals(clazz)) {
                return true;
            }
        }
        return false;
    }
}
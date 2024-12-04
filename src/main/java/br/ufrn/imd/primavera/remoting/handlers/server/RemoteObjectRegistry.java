package br.ufrn.imd.primavera.remoting.handlers.server;


import java.util.HashMap;
import java.util.Map;

import br.ufrn.imd.primavera.remoting.entities.AbsoluteObjectReference;
import br.ufrn.imd.primavera.remoting.entities.ObjectID;


public class RemoteObjectRegistry {
    private static RemoteObjectRegistry instance;
    private final Map<ObjectID, Object> registry = new HashMap<>();

    private RemoteObjectRegistry() {}

    public static synchronized RemoteObjectRegistry getInstance() {
        if (instance == null) {
            instance = new RemoteObjectRegistry();
        }
        return instance;
    }

    public AbsoluteObjectReference register(Object object, String host, int port, String protocol) {
        ObjectID objectId = new ObjectID();
        registry.put(objectId, object);
        System.out.println("Registrado: " + objectId);
        return new AbsoluteObjectReference(host, port, protocol, objectId);
    }

    public Object lookup(ObjectID objectId) {
        Object result = registry.get(objectId);
        if (result != null) {
            System.out.println("Lookup bem-sucedido para: " + objectId);
        } else {
            System.out.println("Falha no lookup: " + objectId);
        }
        return result;
    }

    public void unregister(ObjectID objectId) {
        registry.remove(objectId);
        System.out.println("Removido: " + objectId);
    }
}
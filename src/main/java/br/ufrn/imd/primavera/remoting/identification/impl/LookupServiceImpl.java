package br.ufrn.imd.primavera.remoting.identification.impl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import br.ufrn.imd.primavera.remoting.entities.AbsoluteObjectReference;
import br.ufrn.imd.primavera.remoting.entities.ObjectID;
import br.ufrn.imd.primavera.remoting.identification.LookupService;

public class LookupServiceImpl extends UnicastRemoteObject implements LookupService {

    // Mapa que armazena os objetos registrados, com o path como chave
    private Map<String, AbsoluteObjectReference> registry;

    public LookupServiceImpl() throws RemoteException {
        super();
        registry = new HashMap<>();
    }

    @Override
    public void registerObject(String path, AbsoluteObjectReference aor) throws RemoteException {
        // Registra o objeto usando o path como chave
        registry.put(path, aor);
        System.out.println("Objeto registrado com path: " + path + " -> " + aor);
    }

    @Override
    public AbsoluteObjectReference lookup(String path) throws RemoteException {
        // Realiza o lookup do objeto com base no path
        AbsoluteObjectReference aor = registry.get(path);
        if (aor != null) {
            System.out.println("AOR encontrado para o path: " + path + " -> " + aor);
        } else {
            System.out.println("AOR não encontrado para o path: " + path);
        }
        return aor;
    }

    @Override
    public Remote resolveObject(ObjectID objectId) throws RemoteException {
        // Obtém o nome da classe do ObjectID como chave
        String key = objectId.getClazz().getName();

        // Verifica se o mapa contém o ObjectID como chave
        if (!registry.containsKey(key)) {
            throw new RemoteException("ObjectID não encontrado: " + objectId);
        }

        // Obtém a referência de objeto absoluto
        AbsoluteObjectReference aor = registry.get(key);
        if (aor == null) {
            throw new RemoteException("Referência de objeto absoluto é nula para o ObjectID: " + objectId);
        }

        // Retorna o objeto remoto associado ao AOR
        Object remoteObject = aor.getObjectId();
        if (!(remoteObject instanceof Remote)) {
            throw new RemoteException("O objeto associado ao ObjectID não implementa Remote: " + objectId);
        }

        return (Remote) remoteObject;
    }

}
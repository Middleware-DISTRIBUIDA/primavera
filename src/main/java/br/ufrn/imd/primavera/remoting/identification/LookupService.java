package br.ufrn.imd.primavera.remoting.identification;

import java.rmi.Remote;
import java.rmi.RemoteException;

import br.ufrn.imd.primavera.remoting.entities.AbsoluteObjectReference;
import br.ufrn.imd.primavera.remoting.entities.ObjectID;

public interface LookupService extends Remote {
    // Método para registrar um objeto remoto com uma chave (nome único ou
    // propriedades)
    void registerObject(String path, AbsoluteObjectReference aor) throws RemoteException;

    // Método para buscar um objeto remoto baseado em um nome (ou propriedades)
    AbsoluteObjectReference lookup(String path) throws RemoteException, RemoteException;

    Remote resolveObject(ObjectID objectId) throws RemoteException;
}

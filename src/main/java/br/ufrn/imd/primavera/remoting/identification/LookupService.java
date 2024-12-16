package br.ufrn.imd.primavera.remoting.identification;

import java.rmi.Remote;
import java.rmi.RemoteException;

import br.ufrn.imd.primavera.remoting.entities.AbsoluteObjectReference;
import br.ufrn.imd.primavera.remoting.entities.ObjectID;

public interface LookupService extends Remote {
    void registerObject(String path, AbsoluteObjectReference aor) throws RemoteException;

    AbsoluteObjectReference lookup(String path) throws RemoteException;

    Remote resolveObject(ObjectID objectId) throws RemoteException;
}

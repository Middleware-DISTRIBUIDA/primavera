package br.ufrn.imd.primavera.remoting.identification.impl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import br.ufrn.imd.primavera.remoting.entities.AbsoluteObjectReference;
import br.ufrn.imd.primavera.remoting.entities.ObjectID;
import br.ufrn.imd.primavera.remoting.identification.LookupService;
import br.ufrn.imd.primavera.remoting.identification.RemoteObjectRegistry;

public class LookupServiceImpl extends UnicastRemoteObject implements LookupService {
	private static final long serialVersionUID = 3400986542062969021L;
	private static LookupServiceImpl instance;
	private Map<String, AbsoluteObjectReference> registry;
	private RemoteObjectRegistry aors;

	private LookupServiceImpl() throws RemoteException {
		super();
		this.registry = new HashMap<>();
		this.aors = RemoteObjectRegistry.getInstance();
		System.out.println("[INFO] LookupServiceImpl inicializado com sucesso.");
	}

	public static synchronized LookupServiceImpl getInstance() throws RemoteException {
		if (instance == null) {
			instance = new LookupServiceImpl();
		}
		return instance;
	}

	@Override
	public void registerObject(String path, AbsoluteObjectReference aor) throws RemoteException {
		System.out.println("[INFO] Tentando registrar objeto...");
		System.out.println("[DEBUG] Path: " + path);
		System.out.println("[DEBUG] AOR: " + aor);

		registry.put(path, aor);
		System.out.println("[SUCCESS] Objeto registrado com path: " + path + " -> " + aor);
	}

	public void registerObject(String path, String host, int port, Class<?> clazz, Object newInstance)
			throws RemoteException {
		System.out.println("[INFO] Tentando registrar objeto...");
		System.out.println("[DEBUG] Path: " + path);
		System.out.println("[DEBUG] Host: " + host + " | Port: " + port);
		AbsoluteObjectReference aor = aors.getOrRegisterAOR(clazz, newInstance, host, port);

		registry.put(path, aor);

		System.out.println("[SUCCESS] Objeto registrado com path: " + path + " -> " + aor);
	}

	@Override
	public AbsoluteObjectReference lookup(String path) throws RemoteException {
		System.out.println("[INFO] Realizando lookup...");
		System.out.println("[DEBUG] Path: " + path);

		AbsoluteObjectReference aor = registry.get(path);
		if (aor != null) {
			System.out.println("[SUCCESS] AOR encontrado para o path: " + path + " -> " + aor);
		} else {
			System.out.println("[WARN] AOR não encontrado para o path: " + path);
		}
		return aor;
	}

	@Override
	public Remote resolveObject(ObjectID objectId) throws RemoteException {
		System.out.println("[INFO] Resolvendo objeto...");
		System.out.println("[DEBUG] ObjectID: " + objectId);

		String key = objectId.getClazz().getName();
		System.out.println("[DEBUG] Chave derivada do ObjectID: " + key);

		if (!registry.containsKey(key)) {
			System.out.println("[ERROR] ObjectID não encontrado no registro: " + objectId);
			throw new RemoteException("ObjectID não encontrado: " + objectId);
		}

		AbsoluteObjectReference aor = registry.get(key);
		if (aor == null) {
			System.out.println("[ERROR] Referência de objeto absoluto é nula para o ObjectID: " + objectId);
			throw new RemoteException("Referência de objeto absoluto é nula para o ObjectID: " + objectId);
		}

		Object remoteObject = aor.getObjectId();
		if (!(remoteObject instanceof Remote)) {
			System.out.println("[ERROR] O objeto associado ao ObjectID não implementa Remote: " + objectId);
			throw new RemoteException("O objeto associado ao ObjectID não implementa Remote: " + objectId);
		}

		System.out.println("[SUCCESS] Objeto remoto resolvido com sucesso: " + remoteObject);
		return (Remote) remoteObject;
	}
}

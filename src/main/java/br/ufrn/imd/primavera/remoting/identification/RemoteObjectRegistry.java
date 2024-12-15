package br.ufrn.imd.primavera.remoting.identification;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.imd.primavera.remoting.entities.AbsoluteObjectReference;
import br.ufrn.imd.primavera.remoting.entities.ObjectID;

public class RemoteObjectRegistry {
    private static RemoteObjectRegistry instance;
    private final ObjectIDRegistry objectIDRegistry; // Instância do ObjectIDRegistry
    private final List<AbsoluteObjectReference> aors; // Lista de AORs

    // Construtor privado para evitar instância externa
    public RemoteObjectRegistry() {
        this.objectIDRegistry = ObjectIDRegistry.getInstance();
        this.aors = new ArrayList<>();
    }

    // Método para obter a instância única do singleton
    public static synchronized RemoteObjectRegistry getInstance() {
        if (instance == null) {
            instance = new RemoteObjectRegistry();
        }
        return instance;
    }

    // Método para registrar um objeto remoto
    public AbsoluteObjectReference register(Class<?> clazz, Object newInstance, String host, int port) {
        // Adiciona o objeto ao ObjectIDRegistry
        objectIDRegistry.addId(clazz, newInstance);
        ObjectID objectId = new ObjectID(clazz);

        // Verifica se já existe um AOR correspondente
        for (AbsoluteObjectReference aor : aors) {
            if (aor.getObjectId().equals(objectId) &&
                    aor.getHost().equals(host) &&
                    aor.getPort() == port) {
                System.out.println("AOR já registrado: " + aor);
                return aor;
            }
        }

        // Cria um novo AOR e adiciona à lista
        AbsoluteObjectReference newAOR = new AbsoluteObjectReference(host, port, objectId);
        aors.add(newAOR);
        System.out.println("Registrado: " + objectId);

        return newAOR;
    } // Função para verificar se o AOR já está cadastrado ou se precisa ser
      // registrado

    public AbsoluteObjectReference getOrRegisterAOR(Class<?> clazz, Object newInstance, String host, int port) {
        ObjectID objectId = new ObjectID(clazz);

        // Verifica se já existe um AOR correspondente
        for (AbsoluteObjectReference aor : aors) {
            if (aor.getObjectId().equals(objectId) &&
                    aor.getHost().equals(host) &&
                    aor.getPort() == port) {
                System.out.println("AOR já registrado: " + aor);
                return aor;
            }
        }
        AbsoluteObjectReference aor = register(clazz, newInstance, host, port);
        aors.add(aor);
        return aor;
    }

}

package br.ufrn.imd.primavera.remoting.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import br.ufrn.imd.primavera.remoting.entities.AbsoluteObjectReference;
import br.ufrn.imd.primavera.remoting.entities.ObjectID;
import br.ufrn.imd.primavera.remoting.handlers.server.RemoteObjectRegistry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Invoker {
    private static Invoker instance;

    private final RemoteObjectRegistry registry; // Registro de objetos remotos

    private Invoker() {
        this.registry = new RemoteObjectRegistry(); // Inicializa o registro
    }

    public static synchronized Invoker getInstance() {
        if (instance == null) {
            instance = new Invoker();
        }
        return instance;
    }

    // Método para registrar um objeto remoto e retornar o AOR
    public AbsoluteObjectReference registerRemoteObject(Object object, String host, int port, String protocol) {
        return registry.register(object, host, port, protocol);
    }

    // Método para invocar um método de um objeto remoto usando ObjectID
    public Object invoke(ObjectID objectId, String methodName, Class<?>[] parameterTypes, Object... args)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // Localiza o objeto no registro
        Object handlerInstance = registry.lookup(objectId);
        if (handlerInstance == null) {
            throw new IllegalArgumentException("Objeto remoto não encontrado para o ObjectID: " + objectId);
        }

        // Busca o método no objeto
        Method method = handlerInstance.getClass().getMethod(methodName, parameterTypes);

        // Invoca o método
        return method.invoke(handlerInstance, args);
    }

    // Invocação direta (sem ObjectID, como no código original)
    public Object invoke(Method method, Object handlerInstance, Object... args)
            throws IllegalAccessException, InvocationTargetException {
        return method.invoke(handlerInstance, args);
    }

    // Método para remover um objeto remoto do registro
    public void unregisterRemoteObject(ObjectID objectId) {
        registry.unregister(objectId);
    }
}


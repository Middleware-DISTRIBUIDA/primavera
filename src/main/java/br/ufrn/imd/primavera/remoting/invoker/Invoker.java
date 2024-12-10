package br.ufrn.imd.primavera.remoting.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import br.ufrn.imd.primavera.remoting.entities.ObjectID;
import br.ufrn.imd.primavera.remoting.identification.RemoteObjectRegistry;

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

    // Invocação direta (sem ObjectID, como no código original)
    public Object invoke(Method method, Object handlerInstance, Object... args)
            throws IllegalAccessException, InvocationTargetException {
        return method.invoke(handlerInstance, args);
    }

}

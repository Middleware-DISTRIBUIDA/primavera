package br.ufrn.imd.primavera.remoting.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.ufrn.imd.primavera.remoting.identification.ObjectIDRegistry;

public class Invoker {
	private static final Logger logger = LogManager.getLogger();
	private ObjectIDRegistry objectsRegistry;

	private static Invoker instance;

	private Invoker() {
		this.objectsRegistry = ObjectIDRegistry.getInstance();
	}

	public static synchronized Invoker getInstance() {
		if (instance == null) {
			instance = new Invoker();
		}
		return instance;
	}

	public Object invoke(Method method, Class<?> clazz, Object... args)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		Object handlerInstance = getHandlerInstance(clazz);
		return method.invoke(handlerInstance, args);
	}

	private Object getHandlerInstance(Class<?> handlerClass) throws InstantiationException, IllegalAccessException {
		if (objectsRegistry.containsId(handlerClass)) {
			return objectsRegistry.getId(handlerClass);
		}
		try {
			Object newInstance = handlerClass.getDeclaredConstructor().newInstance();
			objectsRegistry.addId(handlerClass, newInstance);
			return newInstance;
		} catch (Exception e) {
			logger.error("Failed to create handler instance for " + handlerClass.getSimpleName(), e);
			return null;
		}
	}
}

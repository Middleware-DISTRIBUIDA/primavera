package br.ufrn.imd.primavera.remoting.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Invoker {
	private static Invoker instance;

	private Invoker() {
	}

	public static synchronized Invoker getInstance() {
		if (instance == null) {
			instance = new Invoker();
		}
		return instance;
	}

	public Object invoke(Method method, Object handlerInstance, Object... args)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		return method.invoke(handlerInstance, args);
	}
}

package br.ufrn.imd.primavera.remoting.invoker;

import br.ufrn.imd.primavera.extension.annotations.InvocationInterceptorClass;
import br.ufrn.imd.primavera.extension.enums.InvocationType;
import br.ufrn.imd.primavera.extension.invocationInterceptor.InvocationInterceptor;
import br.ufrn.imd.primavera.extension.invocationInterceptor.InvocationInterceptorManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Invoker {
	private static Invoker instance;
	private InvocationInterceptorManager invocationInterceptorManager;
	private Invoker() {
	}

	public static synchronized Invoker getInstance() {
		if (instance == null) {
			instance = new Invoker();
		}
		return instance;
	}

	public Object invoke(Method method, Object handlerInstance, String context, Object... args)
			throws IllegalAccessException, InvocationTargetException {

		for (InvocationInterceptor interceptor : invocationInterceptorManager.getInterceptors()) {
			if(interceptor.getClass().isAnnotationPresent(InvocationInterceptorClass.class) &&
					interceptor.getClass().getAnnotation(InvocationInterceptorClass.class).value() ==
							InvocationType.BEFORE_INVOCATION) {

				interceptor.execute(context);
			}
		}

		Object invokedMethod = method.invoke(handlerInstance, args);

		for (InvocationInterceptor interceptor : invocationInterceptorManager.getInterceptors()) {
			if(interceptor.getClass().isAnnotationPresent(InvocationInterceptorClass.class) &&
					interceptor.getClass().getAnnotation(InvocationInterceptorClass.class).value() ==
							InvocationType.AFTER_INVOCATION) {
				interceptor.execute(context);
			}
		}

		return invokedMethod;
	}
}

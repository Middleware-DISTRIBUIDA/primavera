package br.ufrn.imd.primavera.extension.invocationInterceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import br.ufrn.imd.primavera.extension.invocationInterceptor.annotations.Interceptor;
import br.ufrn.imd.primavera.extension.invocationInterceptor.entities.AbstractInterceptedData;
import br.ufrn.imd.primavera.extension.invocationInterceptor.entities.InterceptedRequest;
import br.ufrn.imd.primavera.extension.invocationInterceptor.entities.InterceptedResponse;
import br.ufrn.imd.primavera.extension.invocationInterceptor.enums.InvocationType;

public class InvocationInterceptorManager {
	private List<Class<?>> beforeInterceptors;
	private List<Class<?>> afterInterceptors;
	private static InvocationInterceptorManager instance;

	public InvocationInterceptorManager() {
		this.beforeInterceptors = new ArrayList<>();
		this.afterInterceptors = new ArrayList<>();
	}

	public void addBeforeInterceptor(Class<?> interceptor) {
		beforeInterceptors.add(interceptor);
	}

	public void addAfterInterceptor(Class<?> interceptor) {
		afterInterceptors.add(interceptor);
	}

	public List<Class<?>> getBeforeInterceptorsInterceptors() {
		return Collections.unmodifiableList(beforeInterceptors);
	}

	public List<Class<?>> getAfterInterceptorsInterceptors() {
		return Collections.unmodifiableList(afterInterceptors);
	}

	public void invokeBeforeInterceptors(InterceptedRequest ir)
			throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		for (Class<?> interceptor : beforeInterceptors) {
			Method invokerMethod = interceptor.getMethod("intercept", AbstractInterceptedData.class);
			Object s = invokerMethod.getDeclaringClass().getDeclaredConstructor().newInstance();
			invokerMethod.invoke(s, ir);
		}
	}

	public void invokeAfterInterceptors(InterceptedResponse ir)
			throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		for (Class<?> interceptor : afterInterceptors) {
			Method invokerMethod = interceptor.getMethod("intercept", AbstractInterceptedData.class);
			Object s = invokerMethod.getDeclaringClass().getDeclaredConstructor().newInstance();
			invokerMethod.invoke(s, ir);
		}
	}

	public static synchronized InvocationInterceptorManager getInstance() {
		if (instance == null) {
			instance = new InvocationInterceptorManager();
		}
		return instance;
	}

	public void loadInterceptors() {
		Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages("")
				.addScanners(Scanners.TypesAnnotated, Scanners.MethodsAnnotated));
		Set<Class<?>> invocationInterceptorClasses = reflections.getTypesAnnotatedWith(Interceptor.class);

		for (Class<?> interceptorClass : invocationInterceptorClasses) {
			Interceptor annotation = interceptorClass.getAnnotation(Interceptor.class);
			int priority = annotation.priority();
			if (priority < 0) {
				throw new IllegalStateException(
						String.format("Invalid priority on %s definition", interceptorClass.getName()));
			}
			if (annotation.value() == InvocationType.BEFORE_INVOCATION) {
				addBeforeInterceptor(interceptorClass);
			} else if (annotation.value() == InvocationType.AFTER_INVOCATION) {
				addAfterInterceptor(interceptorClass);
			}
		}

		Comparator<Class<?>> comparator = (i1, i2) -> {
			Integer i1Priority = i1.getAnnotation(Interceptor.class).priority();
			Integer i2Priority = i2.getAnnotation(Interceptor.class).priority();

			if (i1Priority == 0 && i2Priority > 0) {
				return 1;
			}

			if (i2Priority == 0 && i1Priority > 0) {
				return -1;
			}

			return i1Priority.compareTo(i2Priority);
		};

		beforeInterceptors.sort(comparator);

		afterInterceptors.sort(comparator);
	}
}

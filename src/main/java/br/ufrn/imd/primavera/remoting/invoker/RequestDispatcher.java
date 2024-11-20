package br.ufrn.imd.primavera.remoting.invoker;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import br.ufrn.imd.primavera.remoting.annotations.BodyParam;
import br.ufrn.imd.primavera.remoting.annotations.Endpoint;
import br.ufrn.imd.primavera.remoting.annotations.Handler;
import br.ufrn.imd.primavera.remoting.annotations.HeaderParam;
import br.ufrn.imd.primavera.remoting.annotations.PathParam;
import br.ufrn.imd.primavera.remoting.annotations.QueryParam;
import br.ufrn.imd.primavera.remoting.entities.ResponseWrapper;
import br.ufrn.imd.primavera.remoting.enums.Verb;
import br.ufrn.imd.primavera.remoting.marshaller.MarshallerFactory;
import br.ufrn.imd.primavera.remoting.marshaller.MarshallerType;
import br.ufrn.imd.primavera.remoting.marshaller.exceptions.SerializationException;
import br.ufrn.imd.primavera.remoting.marshaller.interfaces.Marshaller;

public class RequestDispatcher {
	private static final Logger logger = LogManager.getLogger();
	private static RequestDispatcher instance;

	private Set<Method> methods;

	private final Map<Class<?>, Object> sharedInstances = new HashMap<>();
	private final Invoker invoker;

	private RequestDispatcher() {
		this.methods = new HashSet<>();
		this.invoker = Invoker.getInstance();
	}

	public static synchronized RequestDispatcher getInstance() {
		if (instance == null) {
			instance = new RequestDispatcher();
		}
		return instance;
	}

	public void loadMethods(String... packagesName) {
		Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(packagesName)
				.addScanners(Scanners.TypesAnnotated, Scanners.MethodsAnnotated));

		Set<Class<?>> handlerClasses = reflections.getTypesAnnotatedWith(Handler.class);

		this.methods = new HashSet<>();

		for (Class<?> handlerClass : handlerClasses) {
			for (Method method : handlerClass.getDeclaredMethods()) {
				if (method.isAnnotationPresent(Endpoint.class)) {
					validateReturnType(method);
					this.methods.add(method);
				}
			}
		}
	}

	private void validateReturnType(Method method) {
		if (!ResponseWrapper.class.isAssignableFrom(method.getReturnType())) {
			throw new IllegalStateException(String.format("The method %s.%s must return a ResponseWrapper object.",
					method.getDeclaringClass().getName(), method.getName()));
		}
	}

	public void printMethods() {
		Map<Class<?>, List<Method>> methodsByClass = methods.stream()
				.collect(Collectors.groupingBy(Method::getDeclaringClass));

		System.out.println("Application endpoints:\n");

		for (Map.Entry<Class<?>, List<Method>> entry : methodsByClass.entrySet()) {
			Class<?> clazz = entry.getKey();
			System.out.println("\t" + clazz.getSimpleName());
			for (Method method : entry.getValue()) {
				Endpoint endpointAnnotation = method.getAnnotation(Endpoint.class);
				System.out.printf("\t\t%-6s %s\n", endpointAnnotation.method(), endpointAnnotation.path());
			}
			System.out.println();
		}
		System.out.println();
	}

	public Object dispatchRequest(Verb httpMethod, String path, String body, Map<String, String> headers) {
		for (Method method : methods) {
			Handler handlerClass = method.getDeclaringClass().getAnnotation(Handler.class);
			Endpoint endpoint = method.getAnnotation(Endpoint.class);
			String pathPattern = handlerClass.basePath() + endpoint.path();

			if (pathPattern.startsWith("/") && pathPattern.length() == 0) {
				pathPattern = "";
			} else if (pathPattern.startsWith("/") && pathPattern.length() >= 1) {
				pathPattern = pathPattern.substring(1, pathPattern.length());
			}

			if (path.startsWith("/") && path.length() == 0) {
				path = "";
			} else if (path.startsWith("/") && path.length() >= 1) {
				path = path.substring(1, path.length());
			}

			Map<String, String> queryParams = getQueryParams(path);

			if (endpoint.method() == httpMethod && pathMatchesPattern(pathPattern, path)) {
				try {

					Object handlerInstance = getHandlerInstance(method.getDeclaringClass());

					Object deserializedBody = deserializeBodyIfRequired(method, body);

					Object[] args = resolveMethodArguments(method, pathPattern, path, queryParams, headers,
							deserializedBody);

					return invoker.invoke(method, handlerInstance, args);

				} catch (InvocationTargetException e) {
					logger.error("Error invoking endpoint method: " + method.getName(), e.getCause());
				} catch (IllegalAccessException | InstantiationException e) {
					logger.error("Error accessing endpoint method: " + method.getName(), e);
				} catch (IOException | SerializationException e) {
					logger.error("Error deserializing request body for method: " + method.getName(), e);
				}
			}
		}
		logger.warn("No matching endpoint found for " + httpMethod + " " + path);
		return null;
	}

	private Object deserializeBodyIfRequired(Method method, String body) throws IOException, SerializationException {
		int contFields = 0;
		for (Annotation[] paramAnnotations : method.getParameterAnnotations()) {
			for (Annotation annotation : paramAnnotations) {
				if (annotation instanceof BodyParam) {
					Class<?> targetClass = method.getParameterTypes()[contFields];
					@SuppressWarnings("unchecked")
					Marshaller<String> m = (Marshaller<String>) MarshallerFactory.getMarshaller(MarshallerType.JSON);
					return m.unmarshal(body, targetClass);
				}
				contFields++;
			}
		}
		return body;
	}

	private boolean pathMatchesPattern(String pathPattern, String path) {
		int indexQueryParam = path.indexOf("?");
		if (indexQueryParam > 0) {
			path = path.substring(0, path.indexOf("?"));
		}

		String[] patternSegments = pathPattern.split("/");
		String[] pathSegments = path.split("/");

		if (patternSegments.length != pathSegments.length) {
			return false;
		}

		for (int i = 0; i < pathSegments.length; i++) {
			String patternSegmentUnit = patternSegments[i];
			String pathSegmentUnit = pathSegments[i];

			if (patternSegmentUnit.equals(pathSegmentUnit)) {
				continue;
			}

			if (patternSegmentUnit.startsWith("{") && patternSegmentUnit.endsWith("}")) {
				continue;
			}

			return false;
		}

		return true;
	}

	private Object[] resolveMethodArguments(Method method, String pathPattern, String path,
			Map<String, String> queryParams, Map<String, String> headers, Object body) {
		Annotation[][] paramAnnotations = method.getParameterAnnotations();
		Object[] args = new Object[paramAnnotations.length];

		String pathWithoutQuery = path;

		int indexQueryParam = path.indexOf("?");
		if (indexQueryParam > 0) {
			pathWithoutQuery = path.substring(0, path.indexOf("?"));
		}

		for (int i = 0; i < paramAnnotations.length; i++) {
			for (Annotation annotation : paramAnnotations[i]) {
				if (annotation instanceof PathParam) {
					String paramName = ((PathParam) annotation).value();
					args[i] = extractPathParamValue(pathPattern, pathWithoutQuery, paramName);

				} else if (annotation instanceof QueryParam) {
					String paramName = ((QueryParam) annotation).value();
					args[i] = queryParams.getOrDefault(paramName, null);

				} else if (annotation instanceof HeaderParam) {
					String paramName = ((HeaderParam) annotation).value();
					args[i] = headers.getOrDefault(paramName, null);

				} else if (annotation instanceof BodyParam) {
					args[i] = body;
				}
			}
		}
		return args;
	}

	private Object extractPathParamValue(String pathPattern, String path, String paramName) {
		String regexPattern = pathPattern.replaceAll("\\{([^/]+)\\}", "(?<$1>[^/]+)");
		Pattern pattern = Pattern.compile(regexPattern);
		Matcher matcher = pattern.matcher(path);

		if (matcher.matches()) {
			return matcher.group(paramName);
		}

		return null;
	}

	private Object getHandlerInstance(Class<?> handlerClass) throws InstantiationException, IllegalAccessException {
		return sharedInstances.computeIfAbsent(handlerClass, clazz -> {
			try {
				return clazz.getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				logger.error("Failed to create handler instance for " + handlerClass.getSimpleName(), e);
				return null;
			}
		});
	}

	private Map<String, String> getQueryParams(String url) {
		Map<String, String> queryParams = new HashMap<>();

		int queryStart = url.indexOf("?");
		if (queryStart == -1 || queryStart == url.length() - 1) {
			return queryParams;
		}

		String queryString = url.substring(queryStart + 1);

		String[] pairs = queryString.split("&");
		for (String pair : pairs) {
			String[] keyValue = pair.split("=", 2);
			String key = keyValue[0];
			String value = keyValue.length > 1 ? keyValue[1] : "";
			queryParams.put(key, value);
		}

		return queryParams;
	}
}

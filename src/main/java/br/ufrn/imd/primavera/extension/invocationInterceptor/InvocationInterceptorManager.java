package br.ufrn.imd.primavera.extension.invocationInterceptor;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class InvocationInterceptorManager {
    private List<Class<?>> beforeInterceptors;
    private List<Class<?>> afterInterceptors;
    private static InvocationInterceptorManager instance;

    public void addBeforeInterceptor(Class<?> interceptor) {
        beforeInterceptors.add(interceptor);
    }
    public void addAfterInterceptor(Class<?> interceptor) {
        afterInterceptors.add(interceptor);
    }
    public List<Class<?>> getBeforeInterceptorsInterceptors() {
        return beforeInterceptors;
    }
    public List<Class<?>> getAfterInterceptorsInterceptors() {
        return afterInterceptors;
    }

    public void executeBeforeInterceptors(String request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Class<?> interceptor : beforeInterceptors) {
            interceptor.getMethod("execute", String.class).invoke(request);
        }
    }
    public void executeAfterInterceptors(String request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Class<?> interceptor : afterInterceptors) {
            interceptor.getMethod("execute", String.class).invoke(request);
        }
    }

    public static InvocationInterceptorManager getInstance() {
        if (instance == null) {
            instance = new InvocationInterceptorManager();
        }
        return instance;
    }
}

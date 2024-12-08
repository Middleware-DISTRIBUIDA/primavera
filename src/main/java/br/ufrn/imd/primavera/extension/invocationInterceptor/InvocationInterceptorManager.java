package br.ufrn.imd.primavera.extension.invocationInterceptor;

import java.util.List;

public class InvocationInterceptorManager {
    private List<InvocationInterceptor> interceptors;

    public void addInterceptor(InvocationInterceptor interceptor) {
        interceptors.add(interceptor);
    }
    public List<InvocationInterceptor> getInterceptors() {
        return interceptors;
    }

    public void executeInterceptors(String request) {
        for (InvocationInterceptor interceptor : interceptors) {
            interceptor.execute(request);
        }
    }
}

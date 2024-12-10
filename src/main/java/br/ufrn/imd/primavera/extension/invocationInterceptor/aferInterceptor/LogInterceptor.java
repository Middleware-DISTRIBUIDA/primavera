package br.ufrn.imd.primavera.extension.invocationInterceptor.aferInterceptor;

import br.ufrn.imd.primavera.extension.invocationInterceptor.annotations.InvocationInterceptorClass;
import br.ufrn.imd.primavera.extension.invocationInterceptor.enums.InvocationType;
import br.ufrn.imd.primavera.extension.invocationInterceptor.InvocationInterceptor;
import org.slf4j.Logger;

@InvocationInterceptorClass(InvocationType.AFTER_INVOCATION)
public class LogInterceptor implements InvocationInterceptor {
    private Logger log;

    @Override
    public void execute(String request) {
        log.info("LogInterceptor: " + request);
    }
}

package br.ufrn.imd.primavera.extension.invocationInterceptor.aferInterceptor;

import br.ufrn.imd.primavera.extension.annotations.AfterInvocation;
import br.ufrn.imd.primavera.extension.invocationInterceptor.InvocationInterceptor;
import org.slf4j.Logger;

@AfterInvocation
public class LogInterceptor implements InvocationInterceptor {
    private Logger log;

    @Override
    public void execute(String request) {
        log.info("LogInterceptor: " + request);
    }
}

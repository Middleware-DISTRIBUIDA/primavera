package br.ufrn.imd.primavera.extension.invocationInterceptor;

import br.ufrn.imd.primavera.extension.invocationInterceptor.entities.AbstractInterceptedData;

public interface InvocationInterceptor {
	void intercept(AbstractInterceptedData request) throws Exception;
}

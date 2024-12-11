package br.ufrn.imd.primavera.extension.invocationInterceptor.annotations;

import br.ufrn.imd.primavera.extension.invocationInterceptor.enums.InvocationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InvocationInterceptorClass {
    InvocationType value();
}

package br.ufrn.imd.primavera.remoting.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.ufrn.imd.primavera.remoting.enums.Verb;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Endpoint {
	Verb method();

	String path() default "";
} 

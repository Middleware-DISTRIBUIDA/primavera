package br.ufrn.imd.primavera;

import java.lang.annotation.Annotation;

import br.ufrn.imd.primavera.autoconfigure.PrimaveraApplication;

public class PrimaveraRunner {
	public static void run(Class<?> primaryClass, String... args) {
		// Inicialização do contexto da aplicação
		System.out.println("Inicializando a aplicação " + primaryClass.getSimpleName());

		// Adicione aqui a lógica de configuração, como leitura de propriedades, injeção
		// de dependências etc.
		// Exemplo de lógica simples:
		if (primaryClass.isAnnotationPresent((Class<? extends Annotation>) PrimaveraApplication.class)) {
			System.out.println("A anotação @PrimaveraApplication está presente.");
			// Carregue componentes, configure o servidor etc.
		} else {
			throw new IllegalArgumentException("A classe principal precisa estar anotada com @PrimaveraApplication.");
		}

		// Simulação de uma aplicação rodando
		System.out.println("Aplicação " + primaryClass.getSimpleName() + " iniciada com sucesso!");
	}
}

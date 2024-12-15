package br.ufrn.imd.primavera;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import br.ufrn.imd.primavera.configuration.PrimaveraApplication;
import br.ufrn.imd.primavera.configuration.PrimaveraConfiguration;
import br.ufrn.imd.primavera.remoting.handlers.server.ServerHandler;
import br.ufrn.imd.primavera.remoting.invoker.RequestDispatcher;

public class PrimaveraRunner implements Runnable {

	private PrimaveraConfiguration configuration;
	private Class<? extends PrimaveraConfiguration> configurationTemplate;
	private Set<String> packageControllers;

	public PrimaveraRunner() {
		this.packageControllers = new HashSet<>();
	}

	public PrimaveraRunner configureRunner(Class<?> primaryClass) {
		if (!primaryClass.isAnnotationPresent((Class<? extends Annotation>) PrimaveraApplication.class)) {
			throw new IllegalArgumentException("A classe principal precisa estar anotada com @PrimaveraApplication.");
		}
		return this;
	}

	public PrimaveraRunner configureArgs(String... args) {
		return configureArgs(PrimaveraConfiguration.class, args);
	}

	public PrimaveraRunner configureArgs(Class<? extends PrimaveraConfiguration> configurationTemplate,
			String... args) {

		String separator = System.getProperty("file.separator");

		String configFilePath = String.format("src%smain%sresources%sconfig.yml", separator, separator, separator);

		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

		try {
			this.configuration = mapper.readValue(new File(configFilePath), configurationTemplate);
			this.configurationTemplate = configurationTemplate;
		} catch (IOException e) {
			System.out.println("ARQUIVO DE CONFIGURAÇÃO NÃO ENCONTRADO. APLICANDO CONFIGURAÇÕES PADRÃO.");
			this.configuration = PrimaveraConfiguration.getConfig();
			this.configurationTemplate = PrimaveraConfiguration.class;
		}

		return this;
	}

	public PrimaveraRunner configureControllers(String... packagesControllers)
			throws RemoteException, IllegalAccessException, InstantiationException {

		RequestDispatcher rd = RequestDispatcher.getInstance();
		rd.loadMethods(configuration, packagesControllers);
		rd.printMethods();

		this.packageControllers.addAll(packageControllers);

		return this;
	}

	public PrimaveraConfiguration getConfiguration() {
		return this.configuration;
	}

	public Class<? extends PrimaveraConfiguration> getConfigurationTemplate() {
		return configurationTemplate;
	}

	@Override
	public void run() {
		ServerHandler sh = new ServerHandler(configuration);
		sh.start();
	}
}
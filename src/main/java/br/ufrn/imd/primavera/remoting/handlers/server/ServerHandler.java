package br.ufrn.imd.primavera.remoting.handlers.server;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import br.ufrn.imd.primavera.configuration.PrimaveraConfiguration;
import br.ufrn.imd.primavera.extension.protocolPlugin.Plugin;
import br.ufrn.imd.primavera.extension.protocolPlugin.ProtocolPlugin;
import br.ufrn.imd.primavera.extension.protocolPlugin.http.HTTPProtocolPlugin;

public class ServerHandler {
	private PrimaveraConfiguration configuration;

	public ServerHandler(PrimaveraConfiguration configuration) {
		this.configuration = configuration;
	}

	public void start() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		ProtocolPlugin plugin = null;
		int port = configuration.getPort() == null ? 8080 : configuration.getPort();

		if (configuration.getProtocol() == null) {
			plugin = new HTTPProtocolPlugin(port);
		} else {
			plugin = findPlugin(configuration.getProtocol(), port);
		}

		if (plugin != null) {
			plugin.startServer();
		} else {
			throw new IllegalArgumentException("Unsupported protocol: " + configuration.getProtocol());
		}
	}

	private ProtocolPlugin findPlugin(String protocol, int port) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages("")
				.addScanners(Scanners.TypesAnnotated, Scanners.MethodsAnnotated));
		Set<Class<?>> plugins = reflections.getTypesAnnotatedWith(Plugin.class);

		for (Class<?> pluginClass : plugins) {
			Plugin plugin = pluginClass.getAnnotation(Plugin.class);
			String definedProtocol = plugin.protocol();
			if (protocol.equals(definedProtocol)) {
				return (ProtocolPlugin) pluginClass.getDeclaredConstructor(int.class).newInstance(port);
			}
		}
		return null;
	}
}

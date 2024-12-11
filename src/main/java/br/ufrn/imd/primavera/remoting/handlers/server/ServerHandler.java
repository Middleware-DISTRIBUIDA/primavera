package br.ufrn.imd.primavera.remoting.handlers.server;

import br.ufrn.imd.primavera.configuration.PrimaveraConfiguration;
import br.ufrn.imd.primavera.configuration.network.Protocol;
import br.ufrn.imd.primavera.extension.protocolPlugin.ProtocolPlugin;
import br.ufrn.imd.primavera.extension.protocolPlugin.http.HTTPProtocolPlugin;
import br.ufrn.imd.primavera.extension.protocolPlugin.udp.UDPProtocolPlugin;
import br.ufrn.imd.primavera.remoting.handlers.server.impl.HTTPServer;

public class ServerHandler {
	private PrimaveraConfiguration configuration;

	public ServerHandler(PrimaveraConfiguration configuration) {
		this.configuration = configuration;
	}

	public void start() {
		ProtocolPlugin plugin = null;

		if (configuration.getProtocol().equals(Protocol.HTTP)) {
			plugin = new HTTPProtocolPlugin(configuration.getPort());
		} else if (configuration.getProtocol().equals(Protocol.UDP)) {
			plugin = new UDPProtocolPlugin(configuration.getPort());
		}

		if (plugin != null) {
			plugin.startServer();
		} else {
			throw new IllegalArgumentException("Unsupported protocol: " + configuration.getProtocol());
		}
	}
}

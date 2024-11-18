package br.ufrn.imd.primavera.remoting.handlers.server;

import br.ufrn.imd.primavera.configuration.PrimaveraConfiguration;
import br.ufrn.imd.primavera.configuration.network.Protocol;
import br.ufrn.imd.primavera.remoting.handlers.server.impl.HTTPServer;

public class ServerHandler {
	private PrimaveraConfiguration configuration;

	public ServerHandler(PrimaveraConfiguration configuration) {
		this.configuration = configuration;
	}

	public void start() {
		Server s = null;
		
		if (configuration.getProtocol().equals(Protocol.HTTP)) {
			s = new HTTPServer(configuration.getPort());
		}
		
		s.start();
	}
}

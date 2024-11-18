package br.ufrn.imd.primavera.configuration;

import br.ufrn.imd.primavera.configuration.network.Protocol;

public class PrimaveraConfiguration {
	private int port;
	private Protocol protocol;

	public PrimaveraConfiguration() {

	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PrimaveraConfiguration [port=");
		builder.append(port);
		builder.append(", protocol=");
		builder.append(protocol);
		builder.append("]");
		return builder.toString();
	}

	public static PrimaveraConfiguration getConfig() {
		PrimaveraConfiguration config = new PrimaveraConfiguration();
		config.setPort(8080);
		config.setProtocol(Protocol.HTTP);
		return config;
	}
}

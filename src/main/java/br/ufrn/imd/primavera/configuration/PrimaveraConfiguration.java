package br.ufrn.imd.primavera.configuration;

public class PrimaveraConfiguration {
	private Integer port;
	private String protocol;

	public PrimaveraConfiguration() {

	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
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
		config.setProtocol("HTTP");
		return config;
	}
}

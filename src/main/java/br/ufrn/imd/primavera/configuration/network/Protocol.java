package br.ufrn.imd.primavera.configuration.network;

public enum Protocol {
	HTTP("HTTP"),
	UDP("UDP");

	private final String description;

	Protocol(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public static Protocol fromName(String name) {
		for (Protocol type : values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unknown Protocol: " + name);
	}
}
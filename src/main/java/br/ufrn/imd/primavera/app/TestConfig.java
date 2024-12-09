package br.ufrn.imd.primavera.app;

import br.ufrn.imd.primavera.configuration.PrimaveraConfiguration;

public class TestConfig extends PrimaveraConfiguration {
	private String test;

	public TestConfig() {
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TestConfig [test=");
		builder.append(test);
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}

}

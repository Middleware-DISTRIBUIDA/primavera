package br.ufrn.imd.primavera.app;

import br.ufrn.imd.primavera.PrimaveraRunner;
import br.ufrn.imd.primavera.configuration.PrimaveraApplication;

@PrimaveraApplication
public class PrimaveraTestApplication {
	
	public static void main(String[] args) {	
		PrimaveraRunner runner = new PrimaveraRunner()
				.configureRunner(PrimaveraTestApplication.class)
				.configureArgs(TestConfig.class, args)
				.configureControllers("br.ufrn.imd.primavera.app.controllers");

		runner.run();
	}

}
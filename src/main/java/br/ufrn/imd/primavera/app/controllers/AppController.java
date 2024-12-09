package br.ufrn.imd.primavera.app.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.ufrn.imd.primavera.app.TestConfig;
import br.ufrn.imd.primavera.remoting.annotations.BodyParam;
import br.ufrn.imd.primavera.remoting.annotations.Endpoint;
import br.ufrn.imd.primavera.remoting.annotations.Handler;
import br.ufrn.imd.primavera.remoting.annotations.HeaderParam;
import br.ufrn.imd.primavera.remoting.annotations.PathParam;
import br.ufrn.imd.primavera.remoting.annotations.QueryParam;
import br.ufrn.imd.primavera.remoting.entities.ResponseWrapper;
import br.ufrn.imd.primavera.remoting.enums.Verb;

@Handler(basePath = "/handler")
public class AppController {
	protected static final Logger logger = LogManager.getLogger();
	
	@Endpoint(method = Verb.GET, path = "/get/{pathParam}")
	public ResponseWrapper<TestConfig> test(
			@PathParam(value = "pathParam") String pathParam,
			@QueryParam(value = "queryParam") String queryParam, 
			@BodyParam TestConfig body,
			@HeaderParam(value = "User-Agent") String header) {
		
		logger.info(pathParam);
		logger.info(queryParam);
		logger.info(body);
		logger.info(header);
		
		return ResponseWrapper.ok(body);
	}
}

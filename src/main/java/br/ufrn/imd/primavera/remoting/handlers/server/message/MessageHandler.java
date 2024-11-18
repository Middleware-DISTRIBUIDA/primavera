package br.ufrn.imd.primavera.remoting.handlers.server.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.ufrn.imd.primavera.remoting.invoker.RequestDispatcher;

public abstract class MessageHandler implements Runnable {

	protected static final Logger logger = LogManager.getLogger();

	protected String taskName;
	protected RequestDispatcher requestDispatcher;

	public MessageHandler(String taskName) {
		this.taskName = taskName;
		this.requestDispatcher = RequestDispatcher.getInstance();
	}
}

package br.ufrn.imd.primavera.remoting.handlers.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Server extends Thread {

	protected ExecutorService executorHandle;

	protected Integer port;

	public Server(Integer port) {
		this.port = port;
	}

	protected abstract void startServer();

	public Integer getPort() {
		return port;
	}

	@Override
	public final void run() {
		if (executorHandle == null || executorHandle.isTerminated()) {
			executorHandle = Executors.newVirtualThreadPerTaskExecutor();
		}
		startServer();
		executorHandle.shutdown();
	}
}
package br.ufrn.imd.primavera.remoting.handlers.server.impl;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.ufrn.imd.primavera.remoting.handlers.server.Server;
import br.ufrn.imd.primavera.remoting.handlers.server.message.HTTPMessageHandler;

public class HTTPServer extends Server {

	private static final Logger logger = LogManager.getLogger();

	public HTTPServer(Integer port) {
		super(port);
	}

	@Override
	public void startServer() {
		logger.info("HTTP SERVER STARTED ON PORT " + port);
		Random random = new Random(System.currentTimeMillis());

		try (ServerSocket serverSocket = new ServerSocket(port)) {
			while (true) {
				Socket socket = serverSocket.accept();

				String requestName = String.format("%d%d", System.currentTimeMillis(), random.nextLong(1000, 9999));

				Runnable tcpMsgHandler = new HTTPMessageHandler(requestName, socket);

				executorHandle.execute(tcpMsgHandler);

				logger.info(String.format("RECEIVED REQUEST #%s", requestName));
				logger.info(String.format("REQUEST #%s FROM %s:%d scheduled", requestName,
						socket.getInetAddress().toString(), socket.getPort()));
			}
		} catch (Exception e) {
			logger.error("Critical server error", e);
		} finally {
			logger.info("HTTP SERVER TERMINATING...");
		}
	}

}

package br.ufrn.imd.primavera.extension.protocolPlugin.http;

import br.ufrn.imd.primavera.extension.protocolPlugin.ProtocolPlugin;
import br.ufrn.imd.primavera.remoting.handlers.server.message.HTTPMessageHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class HTTPProtocolPlugin implements ProtocolPlugin {
    private static final Logger logger = LogManager.getLogger();
    private final int port;
    private ExecutorService executorHandle;

    public HTTPProtocolPlugin(int port) {
        this.port = port;
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
        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
        } catch (java.lang.IllegalArgumentException e) {
            logger.error(e);
            e.printStackTrace();
        }

        logger.info("HTTP SERVER TERMINATING...");
    }
}

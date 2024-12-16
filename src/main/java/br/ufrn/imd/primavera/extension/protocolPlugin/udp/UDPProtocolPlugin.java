package br.ufrn.imd.primavera.extension.protocolPlugin.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.ufrn.imd.primavera.extension.protocolPlugin.Plugin;
import br.ufrn.imd.primavera.extension.protocolPlugin.ProtocolPlugin;
import br.ufrn.imd.primavera.remoting.handlers.server.message.UDPMessageHandler;

@Plugin(protocol = "UDP")
public class UDPProtocolPlugin implements ProtocolPlugin {
	private static final Logger logger = LogManager.getLogger();
	private final int port;
	private ExecutorService executorHandle;

	public UDPProtocolPlugin(int port) {
		this.port = port;
	}

	@Override
	public void startServer() {
		logger.info("UDP SERVER STARTED ON PORT " + port);

		try (DatagramSocket socket = new DatagramSocket(port)) {
			byte[] buffer = new byte[1024];
			while (true) {
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);

				String requestName = String.format("%d%d", System.currentTimeMillis(), packet.getLength());

				Runnable udpMsgHandler = new UDPMessageHandler(requestName, socket, packet);

				executorHandle.execute(udpMsgHandler);

				logger.info(String.format("RECEIVED REQUEST #%s", requestName));
				logger.info(String.format("REQUEST #%s FROM %s:%d scheduled", requestName,
						packet.getAddress().toString(), packet.getPort()));
			}
		} catch (SocketException e) {
			logger.error("Socket error: " + e.getMessage());
		} catch (IOException e) {
			logger.error("IO error: " + e.getMessage());
		}

		logger.info("UDP SERVER TERMINATING...");
	}
}

package br.ufrn.imd.primavera.remoting.handlers.server.message;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.io.IOException;

public class UDPMessageHandler extends MessageHandler {
    private final DatagramSocket socket;
    private final DatagramPacket packet;

    public UDPMessageHandler(String taskName, DatagramSocket socket, DatagramPacket packet) {
        super(taskName);
        this.socket = socket;
        this.packet = packet;
    }

    @Override
    public void run() {
        try {
            String message = new String(packet.getData(), 0, packet.getLength());
            String responseMessage = "Processed: " + message;
            byte[] responseData = responseMessage.getBytes();

            DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length,
                    packet.getAddress(), packet.getPort());
            socket.send(responsePacket);
        } catch (IOException e) {
            logger.error("Error handling UDP message: " + e.getMessage());
        }
    }
}
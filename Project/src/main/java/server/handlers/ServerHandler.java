package server.handlers;

import annotations.Handler;
import org.apache.log4j.Logger;
import containers.GsonContainer;
import server.enums.Status;
import server.models.UDPMessage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerHandler implements Runnable {
    private final DatagramSocket socket;
    private final DatagramPacket packet;
    private final static Logger logger = Logger.getLogger(ServerHandler.class);

    public ServerHandler(DatagramSocket socket, DatagramPacket packet) {
        this.socket = socket;
        this.packet = packet;
    }

    @Override
    public void run() {
        UDPMessage request = GsonContainer.getGson().fromJson(new String(packet.getData()).trim(), UDPMessage.class);
        UDPMessage response = new UDPMessage(request.getAction(), null, Status.WRONG_UDP_MESSAGE);

        request.setHost(packet.getAddress().getHostAddress());

        System.out.println("Message: " + request.getBody());

        for (Method method : ActionsHandler.class.getMethods()) {
            if (method.isAnnotationPresent(Handler.class)) {
                Handler handler = method.getAnnotation(Handler.class);
                if (handler.value().equals(request.getAction())) {
                    try {
                        response = (UDPMessage) method.invoke(ActionsHandler.class, request);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        byte[] buffer = response.toString().getBytes();

        try {
            socket.send(new DatagramPacket(buffer, buffer.length, packet.getSocketAddress()));
        } catch (IOException e) {
            logger.error(e.getMessage() + " -> Client: " + packet.getAddress().getHostAddress());
        }
    }
}
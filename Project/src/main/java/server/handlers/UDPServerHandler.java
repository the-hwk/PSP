package server.handlers;

import org.apache.log4j.Logger;
import server.GsonContainer;
import server.enums.Status;
import server.models.UDPMessage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServerHandler implements Runnable {
    private final DatagramSocket socket;
    private final DatagramPacket packet;
    private final static Logger logger = Logger.getLogger(UDPServerHandler.class);

    public UDPServerHandler(DatagramSocket socket, DatagramPacket packet) {
        this.socket = socket;
        this.packet = packet;
    }

    @Override
    public void run() {
        UDPMessage request = GsonContainer.getGson().fromJson(new String(packet.getData()).trim(), UDPMessage.class);
        UDPMessage response;

        switch (request.getAction()) {
            case CONNECTION_OPEN:
                response = ActionsHandler.connectionOpen(request);
                break;
            case AUTHENTICATION:
                response = ActionsHandler.authentication(request);
                break;
            case GET_ROOMS:
                response = ActionsHandler.getRooms(request);
                break;
            case GET_MESSAGES:
                response = ActionsHandler.getMessages(request);
                break;
            case GET_USERS:
                response = ActionsHandler.getUsers(request);
                break;
            case ADD_ROOM:
                response = ActionsHandler.addRoom(request);
                break;
            case ADD_MESSAGE:
                response = ActionsHandler.addMessage(request);
                break;
            case CREATE_NOTIFIER:
                response = ActionsHandler.createNotifier(request, packet.getSocketAddress());
                break;
            default:
                response = new UDPMessage(request.getAction(), Status.WRONG_UDP_MESSAGE);
        }

        byte[] buffer = response.toString().getBytes();

        try {
            socket.send(new DatagramPacket(buffer, buffer.length, packet.getSocketAddress()));
        } catch (IOException e) {
            logger.error(e.getMessage() + " -> Client: " + packet.getAddress().getHostAddress());
        }
    }
}
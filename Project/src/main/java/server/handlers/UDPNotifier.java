package server.handlers;

import entities.MessageEntity;
import entities.RoomEntity;
import entities.UserEntity;
import server.GsonContainer;
import server.enums.Action;
import server.enums.Status;
import server.models.UDPMessage;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class UDPNotifier {
    private Map<UserEntity, SocketAddress> clients = new HashMap<>();
    private DatagramSocket socket;

    public static class SingletonHolder {
        public static final UDPNotifier HOLDER_INSTANCE = new UDPNotifier();
    }

    public static UDPNotifier getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    public void init(InetSocketAddress address) throws SocketException {
        socket = new DatagramSocket(address);
    }

    public synchronized void add(UserEntity user, SocketAddress address) {
        clients.put(user, address);
    }

    public synchronized void remove(UserEntity user) {
        clients.remove(user);
    }

    public void notify(RoomEntity room) {
        for (UserEntity user : room.getUsers()) {
            sendData(user, GsonContainer.getGson().toJson(room));
        }
    }

    public void notify(MessageEntity message) {
        for (UserEntity user : message.getToRoom().getUsers()) {
            sendData(user, GsonContainer.getGson().toJson(message));
        }
    }

    private void sendData(UserEntity user, String s) {
        if (clients.containsKey(user)) {
            UDPMessage message = new UDPMessage(Action.NOTIFY, s, Status.OK);

            byte[] buffer = message.toString().getBytes();

            try {
                socket.send(new DatagramPacket(buffer, buffer.length, clients.get(user)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
package server.handlers;

import beans.Message;
import beans.Room;
import beans.User;
import server.Action;
import server.Status;
import server.UDPMessage;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class UDPNotifier {
    private Map<User, SocketAddress> clients = new HashMap<>();
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

    public synchronized void add(User user, SocketAddress address) {
        clients.put(user, address);
    }

    public synchronized void remove(User user) {
        clients.remove(user);
    }

    public void notify(Room room) {
        for (User user : room.getUsers()) {
            if (clients.containsKey(user)) {
                UDPMessage message = new UDPMessage(Action.NOTIFY, Status.OK);
                message.getProperties().put("room", room);

                byte[] buffer = message.toString().getBytes();

                try {
                    socket.send(new DatagramPacket(buffer, buffer.length, clients.get(user)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void notify(Message message) {
        for (User user : message.getTo().getUsers()) {
            if (clients.containsKey(user)) {
                UDPMessage response = new UDPMessage(Action.NOTIFY, Status.OK);
                response.getProperties().put("message", message);

                byte[] buffer = message.toString().getBytes();

                try {
                    socket.send(new DatagramPacket(buffer, buffer.length, clients.get(user)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
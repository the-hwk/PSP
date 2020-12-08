package server.handlers;

import entities.MessageEntity;
import entities.RoomEntity;
import entities.UserEntity;
import entities.dto.MessageDto;
import entities.mappers.RoomMapper;
import entities.mappers.UserMapper;
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
        System.out.println("UDP Notifier started!");
    }

    public synchronized void add(UserEntity user, SocketAddress address) {
        clients.put(user, address);
        System.out.println("Added notifier for client: " + address.toString());
    }

    public synchronized void remove(UserEntity user) {
        clients.remove(user);
    }

    public void notify(RoomEntity room) {
        for (UserEntity user : room.getUsers()) {
            sendData(Action.NOTIFY_ROOM, user, GsonContainer.getGson().toJson(RoomMapper.INSTANCE.roomToDto(room)));
        }
    }

    public void notify(MessageEntity message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId());
        messageDto.setValue(message.getValue());
        messageDto.setDateVal(message.getDateVal());
        messageDto.setFromUser(UserMapper.INSTANCE.userToDto(message.getFromUser()));
        messageDto.setToRoom(RoomMapper.INSTANCE.roomToDto(message.getToRoom()));

        for (UserEntity user : message.getToRoom().getUsers()) {
            sendData(Action.NOTIFY_MESSAGE, user, GsonContainer.getGson().toJson(messageDto));
        }
    }

    private void sendData(Action action, UserEntity user, String s) {
        if (clients.containsKey(user)) {
            UDPMessage message = new UDPMessage(action, s, Status.OK);

            byte[] buffer = message.toString().getBytes();

            try {
                socket.send(new DatagramPacket(buffer, buffer.length, clients.get(user)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
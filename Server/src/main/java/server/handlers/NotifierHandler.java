package server.handlers;

import entities.MessageEntity;
import entities.RoomEntity;
import entities.UserEntity;
import entities.dto.MessageDto;
import entities.mappers.RoomMapper;
import entities.mappers.UserMapper;
import containers.GsonContainer;
import server.enums.Action;
import server.enums.Status;
import server.models.UDPMessage;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * "Центр уведомлений" для подключенных пользователей
 */
public class NotifierHandler {
    private final Map<UserEntity, InetSocketAddress> clients = new HashMap<>();
    private DatagramChannel channel;

    public static class SingletonHolder {
        public static final NotifierHandler HOLDER_INSTANCE = new NotifierHandler();
    }

    public static NotifierHandler getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    /**
     * Производит инициализацию и запуск сервера уведомлений
     * @param address
     * @throws IOException
     */
    public void init(InetSocketAddress address) throws IOException {
        channel = DatagramChannel.open();
        channel.socket().bind(address);
        System.out.println("UDP Notifier started!");
    }

    /**
     * Добавляет пользователя в список on-line пользователей
     * @param user
     * @param address
     */
    public synchronized void add(UserEntity user, InetSocketAddress address) {
        clients.put(user, address);
        System.out.println("Added notifier for client: " + address.toString());
    }

    /**
     * Удаляет пользователя из списка on-line пользователей
     * @param user
     */
    public synchronized void remove(UserEntity user) {
        sendData(Action.CLOSE_NOTIFIER, user, null);
        clients.remove(user);
    }

    public synchronized boolean contains(UserEntity user) {
        return clients.containsKey(user);
    }

    /**
     * Уведомлеяет пользователей о новом добавленном чате.
     * Уведомления отправляются только тем пользователям, которые
     * учавствуют в заданном чате
     * @param room Чат
     */
    public void notify(RoomEntity room) {
        for (UserEntity user : room.getUsers()) {
            sendData(Action.NOTIFY_ROOM, user, GsonContainer.getGson().toJson(RoomMapper.INSTANCE.roomToDto(room)));
        }
    }

    /**
     * Уведомляет пользователей о новом добавленном сообщений.
     * Уведомления отправляются только тем пользователям, которые
     * учавствуют в заданном чате
     * @param message
     */
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

    /**
     * Формирует и отправляет сообщение указанному пользователю
     * @param action Действие
     * @param user Пользователь
     * @param s Данные
     */
    private void sendData(Action action, UserEntity user, String s) {
        if (clients.containsKey(user)) {
            UDPMessage message = new UDPMessage(action, s, Status.OK);

            ByteBuffer buffer = ByteBuffer.wrap(GsonContainer.getGson().toJson(message).getBytes());

            try {
                System.out.println("Notify user " + clients.get(user).getAddress().getHostAddress() + ":" + clients.get(user).getPort());
                channel.send(buffer, clients.get(user));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
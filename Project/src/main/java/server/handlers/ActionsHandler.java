package server.handlers;

import annotations.Handler;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import db.repository.impl.RepositoryFactory;
import entities.MessageEntity;
import entities.PasswordEntity;
import entities.RoomEntity;
import entities.UserEntity;
import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import server.GsonContainer;
import server.enums.Action;
import server.enums.Status;
import server.models.UDPMessage;

import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;

public class ActionsHandler {
    private static final Logger logger = Logger.getLogger(ActionsHandler.class);

    @Handler(Action.CONNECTION_OPEN)
    public static UDPMessage connectionOpen(UDPMessage request) {
        return new UDPMessage(request.getAction(), null, Status.OK);
    }

    @Handler(Action.REGISTRATION)
    public static UDPMessage registration(UDPMessage request) {
        JsonObject packet = JsonParser.parseString(request.getBody()).getAsJsonObject();

        try {
            UserEntity user = GsonContainer.getGson()
                    .fromJson(packet.get("user").getAsString(), UserEntity.class);
            PasswordEntity password = GsonContainer.getGson()
                    .fromJson(packet.get("password").getAsString(), PasswordEntity.class);

            RepositoryFactory.getUserRepository().save(user);
            password.setUser(user);
            RepositoryFactory.getPasswordRepository().save(password);

            return new UDPMessage(request.getAction(), null, Status.OK);
        } catch (ConstraintViolationException e) {
            logger.error(e.getMessage());
            return new UDPMessage(request.getAction(), null, Status.USER_DUPLICATE);
        }
    }

    @Handler(Action.LOGIN)
    public static UDPMessage login(UDPMessage request) {
        JsonObject packet = JsonParser.parseString(request.getBody()).getAsJsonObject();

        UserEntity user = GsonContainer.getGson()
                .fromJson(packet.get("user").getAsString(), UserEntity.class);
        PasswordEntity password = GsonContainer.getGson()
                .fromJson(packet.get("password").getAsString(), PasswordEntity.class);

        UserEntity dbUser = RepositoryFactory.getUserRepository().findByEmail(user.getEmail());
        PasswordEntity dbPassword = RepositoryFactory.getPasswordRepository().findByUser(dbUser);

        if (Arrays.equals(dbPassword.getValue(), password.getValue())) {
            return new UDPMessage(request.getAction(), GsonContainer.getGson().toJson(dbUser), Status.OK);
        } else {
            return new UDPMessage(request.getAction(), null, Status.WRONG_PASSWORD);
        }
    }

    @Handler(Action.CREATE_NOTIFIER)
    public static UDPMessage createNotifier(UDPMessage request, SocketAddress address) {
        UserEntity user = GsonContainer.getGson().fromJson(request.getBody(), UserEntity.class);

        UDPNotifier.getInstance().add(user, address);

        return new UDPMessage(request.getAction(), null, Status.OK);
    }

    @Handler(Action.GET_ROOMS)
    public static UDPMessage getRooms(UDPMessage request) {
        UserEntity user = GsonContainer.getGson().fromJson(request.getBody(), UserEntity.class);
        UserEntity dbUser = RepositoryFactory.getUserRepository().findById(user.getId());

        return new UDPMessage(request.getAction(), GsonContainer.getGson().toJson(dbUser.getRooms()), Status.OK);
    }

    @Handler(Action.GET_MESSAGES)
    public static UDPMessage getMessages(UDPMessage request) {
        RoomEntity room = GsonContainer.getGson().fromJson(request.getBody(), RoomEntity.class);
        RoomEntity dbRoom = RepositoryFactory.getRoomRepository().findById(room.getId());

        return new UDPMessage(request.getAction(), GsonContainer.getGson().toJson(dbRoom.getUsers()), Status.OK);
    }

    @Handler(Action.GET_USERS)
    public static UDPMessage getUsers(UDPMessage request) {
        List<UserEntity> users = RepositoryFactory.getUserRepository().findAll();

        return new UDPMessage(request.getAction(), GsonContainer.getGson().toJson(users), Status.OK);
    }

    @Handler(Action.ADD_ROOM)
    public static UDPMessage addRoom(UDPMessage request) {
        RoomEntity room = GsonContainer.getGson().fromJson(request.getBody(), RoomEntity.class);
        RepositoryFactory.getRoomRepository().save(room);

        UDPNotifier.getInstance().notify(room);

        return new UDPMessage(request.getAction(), GsonContainer.getGson().toJson(room), Status.OK);
    }

    @Handler(Action.ADD_MESSAGE)
    public static UDPMessage addMessage(UDPMessage request) {
        MessageEntity message = GsonContainer.getGson().fromJson(request.getBody(), MessageEntity.class);
        RepositoryFactory.getMessageRepository().save(message);

        UDPNotifier.getInstance().notify(message);

        return new UDPMessage(request.getAction(), GsonContainer.getGson().toJson(message), Status.OK);
    }
}
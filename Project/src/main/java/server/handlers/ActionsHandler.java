package server.handlers;

import annotations.Handler;
import db.repository.impl.RepositoryFactory;
import entities.*;
import entities.dto.MessageDto;
import entities.dto.UserDto;
import entities.mappers.MessageMapper;
import entities.mappers.UserMapper;
import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import server.GsonContainer;
import server.enums.Action;
import server.enums.Status;
import server.models.UDPMessage;

import java.net.SocketAddress;
import java.util.*;

public class ActionsHandler {
    private static final Logger logger = Logger.getLogger(ActionsHandler.class);

    @Handler(Action.CONNECTION_OPEN)
    public static UDPMessage connectionOpen(UDPMessage request) {
        return new UDPMessage(request.getAction(), null, Status.OK);
    }

    @Handler(Action.REGISTRATION)
    public static UDPMessage registration(UDPMessage request) {
        try {
            PasswordEntity password = GsonContainer.getGson()
                    .fromJson(request.getBody(), PasswordEntity.class);

            RepositoryFactory.getUserRepository().save(password.getUser());
            RepositoryFactory.getPasswordRepository().save(password);

            return new UDPMessage(request.getAction(), null, Status.OK);
        } catch (ConstraintViolationException e) {
            logger.error(e.getMessage());
            return new UDPMessage(request.getAction(), null, Status.USER_DUPLICATE);
        }
    }

    @Handler(Action.LOGIN)
    public static UDPMessage login(UDPMessage request) {
        PasswordEntity password = GsonContainer.getGson()
                .fromJson(request.getBody(), PasswordEntity.class);

        UserEntity dbUser = RepositoryFactory.getUserRepository().findByEmail(password.getUser().getEmail());
        PasswordEntity dbPassword = RepositoryFactory.getPasswordRepository().findByUser(dbUser);

        for (RoomEntity room : dbUser.getRooms()) {
            room.setUsers(null);
        }

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

        List<MessageEntity> dbMessages = RepositoryFactory.getMessageRepository().findForRoom(room);

        List<MessageDto> messages = new ArrayList<>();
        for (MessageEntity dbMessage : dbMessages) {
            MessageDto messageDto = new MessageDto();
            messageDto.setId(dbMessage.getId());
            messageDto.setDateVal(dbMessage.getDateVal());
            messageDto.setValue(dbMessage.getValue());
            messageDto.setFromUser(UserMapper.INSTANCE.userToDto(dbMessage.getFromUser()));
            messages.add(messageDto);
        }

        return new UDPMessage(request.getAction(), GsonContainer.getGson().toJson(messages), Status.OK);
    }

    @Handler(Action.GET_USERS)
    public static UDPMessage getUsers(UDPMessage request) {
        List<UserEntity> users = RepositoryFactory.getUserRepository().findAll();

        List<UserDto> userDtos = new ArrayList<>();
        for (UserEntity user : users) {
            userDtos.add(UserMapper.INSTANCE.userToDto(user));
        }

        return new UDPMessage(request.getAction(), GsonContainer.getGson().toJson(userDtos), Status.OK);
    }

    @Handler(Action.ADD_ROOM)
    public static UDPMessage addRoom(UDPMessage request) {
        RoomEntity room = GsonContainer.getGson().fromJson(request.getBody(), RoomEntity.class);

        Set<UserEntity> users = new HashSet<>();

        for (UserEntity user : room.getUsers()) {
            users.add(RepositoryFactory.getUserRepository().findById(user.getId()));
        }

        room.setUsers(users);

        RepositoryFactory.getRoomRepository().save(room);

        // UDPNotifier.getInstance().notify(room);

        return new UDPMessage(request.getAction(), GsonContainer.getGson().toJson(room), Status.OK);
    }

    @Handler(Action.ADD_MESSAGE)
    public static UDPMessage addMessage(UDPMessage request) {
        MessageEntity message = GsonContainer.getGson().fromJson(request.getBody(), MessageEntity.class);
        RepositoryFactory.getMessageRepository().save(message);

        // UDPNotifier.getInstance().notify(message);

        return new UDPMessage(request.getAction(), GsonContainer.getGson().toJson(message), Status.OK);
    }
}
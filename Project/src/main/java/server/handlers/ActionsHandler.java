package server.handlers;

import beans.Message;
import beans.Password;
import beans.Room;
import beans.User;
import db.dao.DaoFactory;
import server.enums.Status;
import server.models.UDPMessage;

import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ActionsHandler {
    private static DaoFactory daoFactory = DaoFactory.getDaoFactory(DaoFactory.Consumer.MYSQL);

    public static UDPMessage connectionOpen(UDPMessage request) {
        return new UDPMessage(request.getAction(), null, Status.OK);
    }

    public static UDPMessage authentication(UDPMessage request) {
        if (!request.getProperties().containsKey("user")
                || !request.getProperties().containsKey("password")) {
            return new UDPMessage(request.getAction(), Status.MISSING_PROPERTY);
        }

        User user = (User) request.getProperties().get("user");
        Password password = (Password) request.getProperties().get("password");

        UDPMessage response;

        Optional<User> dbUser = daoFactory.getUserDao().selectByEmail(user.getEmail());
        if (dbUser.isPresent()) {
            Optional<Password> dbPassword = daoFactory.getPasswordDao().selectByUser(dbUser.get());
            if (dbPassword.isPresent()) {
                if (Arrays.equals(password.getValue(), dbPassword.get().getValue())) {
                    response = new UDPMessage(request.getAction(), Status.OK);
                    response.getProperties().put("user", dbUser.get());
                } else {
                    response = new UDPMessage(request.getAction(), Status.WRONG_PASSWORD);
                }
            } else {
                response = new UDPMessage(request.getAction(), Status.WRONG_PASSWORD);
            }
        } else {
            int id = daoFactory.getUserDao().insert(user);
            if (id != 0) {
                User newUser = new User(id, user);
                password.setUser(newUser);
                if (daoFactory.getPasswordDao().insert(password) != 0) {
                    response = new UDPMessage(request.getAction(), Status.OK);
                    response.getProperties().put("user", newUser);
                } else {
                    response = new UDPMessage(request.getAction(), Status.DB_ERROR);
                }
            } else {
                response = new UDPMessage(request.getAction(), Status.USER_DUPLICATE);
            }
        }

        return response;
    }

    public static UDPMessage createNotifier(UDPMessage request, SocketAddress address) {
        if (!request.getProperties().containsKey("user")) {
            return new UDPMessage(request.getAction(), Status.MISSING_PROPERTY);
        }

        User user = (User) request.getProperties().get("user");

        UDPNotifier.getInstance().add(user, address);

        return new UDPMessage(request.getAction(), Status.OK);
    }

    public static UDPMessage getRooms(UDPMessage request) {
        if (!request.getProperties().containsKey("user")) {
            return new UDPMessage(request.getAction(), Status.MISSING_PROPERTY);
        }

        User user = (User) request.getProperties().get("user");

        List<Room> rooms = daoFactory.getRoomDao().selectByUser(user);

        UDPMessage response = new UDPMessage(request.getAction(), Status.OK);
        response.getProperties().put("rooms", rooms);

        return response;
    }

    public static UDPMessage getMessages(UDPMessage request) {
        if (!request.getProperties().containsKey("room")) {
            return new UDPMessage(request.getAction(), Status.MISSING_PROPERTY);
        }

        Room room = (Room) request.getProperties().get("room");

        List<Message> messages = daoFactory.getMessageDao().selectByRoom(room);

        UDPMessage response = new UDPMessage(request.getAction(), Status.OK);
        response.getProperties().put("messages", messages);

        return response;
    }

    public static UDPMessage getUsers(UDPMessage request) {
        List<User> users = daoFactory.getUserDao().select();

        UDPMessage response = new UDPMessage(request.getAction(), Status.OK);
        response.getProperties().put("users", users);

        return response;
    }

    public static UDPMessage addRoom(UDPMessage request) {
        if (!request.getProperties().containsKey("room")) {
            return new UDPMessage(request.getAction(), Status.MISSING_PROPERTY);
        }

        Room room = (Room) request.getProperties().get("room");

        // TODO: Add room

        UDPNotifier.getInstance().notify(room);

        return new UDPMessage(request.getAction(), Status.OK);
    }

    public static UDPMessage addMessage(UDPMessage request) {
        if (!request.getProperties().containsKey("message")) {
            return new UDPMessage(request.getAction(), Status.MISSING_PROPERTY);
        }

        Message message = (Message) request.getProperties().get("message");

        // TODO: Add Message

        UDPNotifier.getInstance().notify(message);

        return new UDPMessage(request.getAction(), Status.OK);
    }
}
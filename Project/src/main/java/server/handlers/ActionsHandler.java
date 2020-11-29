package server.handlers;

import beans.Message;
import beans.Password;
import beans.Room;
import beans.User;
import server.enums.Status;
import server.models.UDPMessage;

import java.net.SocketAddress;

public class ActionsHandler {
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

        // TODO: Add user and password to Db

        UDPMessage response = new UDPMessage(request.getAction(), Status.OK);
        response.getProperties().put("user", user);

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

        // TODO: Get list of rooms for user

        UDPMessage response = new UDPMessage(request.getAction(), Status.OK);
        // response.getProperties().put("rooms", );

        return response;
    }

    public static UDPMessage getMessages(UDPMessage request) {
        if (!request.getProperties().containsKey("room")) {
            return new UDPMessage(request.getAction(), Status.MISSING_PROPERTY);
        }

        Room room = (Room) request.getProperties().get("room");

        // TODO: Get list of messages for room

        UDPMessage response = new UDPMessage(request.getAction(), Status.OK);
        // response.getProperties().put("messages", );

        return response;
    }

    public static UDPMessage getUsers(UDPMessage request) {
        // TODO: Get list of users

        UDPMessage response = new UDPMessage(request.getAction(), Status.OK);

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
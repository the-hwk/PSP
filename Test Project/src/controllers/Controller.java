package controllers;

import beans.*;
import com.google.gson.reflect.TypeToken;
import info.Connection;
import info.Data;
import info.NotifyListener;
import javafx.application.Platform;
import netscape.javascript.JSObject;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

public class Controller {
    private final JSObject windowObject;

    public Controller(JSObject windowObject) {
        this.windowObject = windowObject;
    }

    public void createConnection(String host) {
        if (Connection.createConnection(host)) {
            windowObject.call("showPage", "login");
        } else {
            windowObject.call("showError", "Не удалось подключиться к серверу");
        }
    }

    public void login(String email, String passwordStr) {
        UserEntity user = new UserEntity();
        user.setEmail(email);

        PasswordEntity password = new PasswordEntity();
        password.setUser(user);
        password.setValue(passwordStr.getBytes());

        UDPMessage request = new UDPMessage(Action.LOGIN, GsonContainer.getGson().toJson(password));
        UDPMessage response = Connection.createRequest(request);

        if (response.getStatus().equals(Status.OK)) {
            windowObject.call("showInfo", "Вы вошли в свой аккаунт");
            windowObject.call("showPage", "main");
            windowObject.call("setUser", response.getBody());
            windowObject.call("renderRooms");

            Data.setUser(GsonContainer.getGson().fromJson(response.getBody(), UserEntity.class));

            NotifyListener listener = new NotifyListener(Connection.getAddress().getHostName(), this);

            String message = response.getBody() + "\n" + listener.getHost() + "\n" + listener.getPort();
            request = new UDPMessage(Action.CREATE_NOTIFIER, message);
            response = Connection.createRequest(request);

            if (response.getStatus().equals(Status.OK)) {
                new Thread(listener).start();
            } else {
                windowObject.call("showError", "Неверные данные для входа");
            }
        }
    }

    public void registration(String email, String name, String passwordStr) {
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setNickname(name);

        PasswordEntity password = new PasswordEntity();
        password.setUser(user);
        password.setValue(passwordStr.getBytes());

        UDPMessage request = new UDPMessage(Action.REGISTRATION, GsonContainer.getGson().toJson(password));
        UDPMessage response = Connection.createRequest(request);

        if (response.getStatus().equals(Status.OK)) {
            windowObject.call("showInfo", "Вы зарегистрированы");
            windowObject.call("showPage", "login");
        } else if (response.getStatus().equals(Status.USER_DUPLICATE)) {
            windowObject.call("showError", "Такой пользователь уже существует");
        } else {
            windowObject.call("showError", "Ошибка на сервере");
        }
    }

    public void getUsers() {
        UDPMessage request = new UDPMessage(Action.GET_USERS, null);
        UDPMessage response = Connection.createRequest(request);

        if (response.getStatus().equals(Status.OK)) {
            windowObject.call("fillUsersSelect", response.getBody());
        } else {
            windowObject.call("showError", "Не удалось получить данные о пользователях");
        }
    }

    public String getMessages(int roomId) {
        RoomEntity room = new RoomEntity();
        room.setId(roomId);

        UDPMessage request = new UDPMessage(Action.GET_MESSAGES, GsonContainer.getGson().toJson(room));
        UDPMessage response = Connection.createRequest(request);

        if (response.getStatus().equals(Status.OK)) {
            return response.getBody();
        } else {
            System.out.println("error");
            return null;
        }
    }

    public void createRoom(String name, String usersIdJson) {
        Set<UserEntity> users = GsonContainer.getGson().fromJson(usersIdJson, new TypeToken<Set<UserEntity>>() {}.getType());

        RoomEntity room = new RoomEntity();
        room.setName(name); 
        room.setUsers(users);

        UDPMessage request = new UDPMessage(Action.ADD_ROOM, GsonContainer.getGson().toJson(room));
        UDPMessage response = Connection.createRequest(request);

        if (response.getStatus().equals(Status.OK)) {
            windowObject.call("showInfo", "Чат добавлен");
            windowObject.call("showPage", "main");
        } else {
            windowObject.call("showError", "Не удалось добавить чат");
        }
    }

    public void createMessage(int userId, int roomId, String value) {
        UserEntity user = new UserEntity();
        user.setId(userId);

        RoomEntity room = new RoomEntity();
        room.setId(roomId);

        MessageEntity message = new MessageEntity();
        message.setDateVal(new Timestamp(new Date().getTime()));
        message.setFromUser(user);
        message.setValue(value);
        message.setToRoom(room);

        UDPMessage request = new UDPMessage(Action.ADD_MESSAGE, GsonContainer.getGson().toJson(message));
        UDPMessage response = Connection.createRequest(request);

        if (!response.getStatus().equals(Status.OK)) {
            windowObject.call("showError", "Ошибка отправки сообщения");
        }
    }

    public void notify(MessageEntity message) {
        Platform.runLater(() -> {
            windowObject.call("notifyMessage", GsonContainer.getGson().toJson(message));
        });
    }

    public void notify(RoomEntity room) {
        Platform.runLater(() -> {
            windowObject.call("addRoomFromJson", GsonContainer.getGson().toJson(room));
            windowObject.call("notifyRoom", GsonContainer.getGson().toJson(room));
        });
    }
}
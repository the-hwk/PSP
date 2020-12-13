package test;

import beans.MessageEntity;
import beans.PasswordEntity;
import beans.RoomEntity;
import beans.UserEntity;
import containers.GsonContainer;
import data.AppConfig;
import data.Data;
import data.UDPMessage;
import enums.Action;
import enums.Status;
import handlers.ConnectionHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

class ConnectionHandlerTest {
    @Test
    @BeforeAll
    static void connectionOpen() {
        AppConfig.getInstance().init();
        UDPMessage response = ConnectionHandler.createConnection("localhost");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Status.OK, response.getStatus());
    }

    @Test
    @AfterAll
    static void connectionClose() {
        UDPMessage request = new UDPMessage(Action.CONNECTION_CLOSE,
                GsonContainer.getGson().toJson(Data.getUser()));

        UDPMessage response = ConnectionHandler.createRequest(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Status.OK, response.getStatus());
    }

    @Test
    void login() {
        UserEntity user = new UserEntity();
        user.setEmail("test@mail.ru");

        PasswordEntity password = new PasswordEntity();
        password.setUser(user);
        password.setValue("pass".getBytes());

        UDPMessage request = new UDPMessage(Action.LOGIN, GsonContainer.getGson().toJson(password));
        UDPMessage response = ConnectionHandler.createRequest(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Status.OK, response.getStatus());

        Data.setUser(GsonContainer.getGson().fromJson(response.getBody(), UserEntity.class));
    }

    @Test
    void getUsers() {
        UDPMessage request = new UDPMessage(Action.GET_USERS, null);
        UDPMessage response = ConnectionHandler.createRequest(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Status.OK, response.getStatus());
    }

    @Test
    void getMessages() {
        RoomEntity room = new RoomEntity();
        room.setId(1);

        UDPMessage request = new UDPMessage(Action.GET_MESSAGES, GsonContainer.getGson().toJson(room));
        UDPMessage response = ConnectionHandler.createRequest(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Status.OK, response.getStatus());
    }

    @Test
    void createRoom() {
        UserEntity user = new UserEntity();
        user.setId(1);

        Set<UserEntity> users = new HashSet<>();
        users.add(user);

        RoomEntity room = new RoomEntity();
        room.setName("client_test_room");
        room.setUsers(users);

        UDPMessage request = new UDPMessage(Action.ADD_ROOM, GsonContainer.getGson().toJson(room));
        UDPMessage response = ConnectionHandler.createRequest(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Status.OK, response.getStatus());
    }

    @Test
    void createMessage() {
        UserEntity user = new UserEntity();
        user.setId(1);

        RoomEntity room = new RoomEntity();
        room.setId(1);

        MessageEntity message = new MessageEntity();
        message.setDateVal(new Timestamp(new Date().getTime()));
        message.setFromUser(user);
        message.setValue("Test client message");
        message.setToRoom(room);

        UDPMessage request = new UDPMessage(Action.ADD_MESSAGE, GsonContainer.getGson().toJson(message));
        UDPMessage response = ConnectionHandler.createRequest(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Status.OK, response.getStatus());
    }
}
package sample;

import beans.*;
import info.Connection;
import info.Data;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

public class Controller {
    private final WebEngine engine;
    private JSObject windowObject;

    public Controller(WebEngine engine, JSObject windowObject) {
        this.engine = engine;
        this.windowObject = windowObject;
    }

    public void createConnection(String host) {
        if (Connection.createConnection(host)) {
            engine.load(this.getClass().getResource("views/auth.html").toExternalForm());
        } else {
            // TODO: JS Error
        }
    }

    public void auth(String email, String password) {
        User user = new User(email);
        Password userPassword = new Password(password.getBytes());

        UDPMessage request = new UDPMessage(Action.AUTHENTICATION);
        request.getProperties().put("user", user);
        request.getProperties().put("password", userPassword);

        UDPMessage response = Connection.createRequest(request);

        if (response.getStatus().equals(Status.OK)) {
            Data.setUser((User) response.getProperties().get("user"));
            windowObject.call("setUser", GsonContainer.getGson().toJson(user));
            engine.load(this.getClass().getResource("views/main.html").toExternalForm());
        } else {
            // TODO: JS Error
        }
    }

    public void register(String email, String name, String password) {
        User user = new User(email, name);
        Password userPassword = new Password(password.getBytes());

        UDPMessage request = new UDPMessage(Action.AUTHENTICATION);
        request.getProperties().put("user", user);
        request.getProperties().put("password", userPassword);

        UDPMessage response = Connection.createRequest(request);

        if (response.getStatus().equals(Status.OK)) {
            // TODO: Call JS set Auth form
        } else {
            // TODO: JS Error
        }
    }

    public String getRooms() {
        UDPMessage request = new UDPMessage(Action.GET_ROOMS);
        request.getProperties().put("user", Data.getUser());

        UDPMessage response = Connection.createRequest(request);

        if (response.getStatus().equals(Status.OK)) {
            return GsonContainer.getGson().toJson(response.getProperties().get("rooms"));
        }

        return null;
    }

    public String getMessages(int roomId) {
        Room room = new Room(roomId);

        UDPMessage request = new UDPMessage(Action.GET_MESSAGES);
        request.getProperties().put("room", room);

        UDPMessage response = Connection.createRequest(request);

        if (response.getStatus().equals(Status.OK)) {
            return GsonContainer.getGson().toJson(response.getProperties().get("messages"));
        }

        return null;
    }
}
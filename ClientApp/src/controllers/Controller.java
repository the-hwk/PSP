package controllers;

import javafx.scene.web.WebEngine;

public class Controller {
    private final WebEngine engine;

    public Controller(WebEngine engine) {
        this.engine = engine;
    }

    public void createConnection() {
        engine.load(this.getClass().getResource("views/main.html").toExternalForm());
        /*
        if (Connection.createConnection(host)) {
            engine.load(this.getClass().getResource("views/auth.html").toExternalForm());
        } else {
            // TODO: JS Error
        }*/
    }
}

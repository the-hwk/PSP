package controllers;

import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

public class Controller {
    private WebEngine engine;

    public Controller(WebEngine engine) {
        this.engine = engine;
    }

    public void createConnection() throws InterruptedException {
        System.out.println("Connection");
        engine.load(this.getClass().getResource("../views/main.html").toExternalForm());
        JSObject windowObject = (JSObject) engine.executeScript("window");
        windowObject.setMember("app", new Object() {
            public void myMethod(){
                this.myMethod();
            }
        });
        /*
        if (Connection.createConnection(host)) {
            engine.load(this.getClass().getResource("views/auth.html").toExternalForm());
        } else {
            // TODO: JS Error
        }*/
    }

    public void check() {
        System.out.println("lol");
    }
}

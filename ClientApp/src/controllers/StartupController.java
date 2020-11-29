package controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import tools.ResourceHandler;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class StartupController {
    @FXML
    private WebView webView;
    private WebEngine engine;

    @FXML
    private void initialize() throws URISyntaxException, MalformedURLException {
        engine = webView.getEngine();
        engine.load(ResourceHandler.getInstance().getURI("startup.html").toURL().toString());
        engine.executeScript("some()");
    }
}

package server.handlers.actions;

import beans.Request;
import beans.Response;
import exceptions.WrongMessageFormatException;

public interface ActionHandler {
    Response process(Request request) throws WrongMessageFormatException;
}

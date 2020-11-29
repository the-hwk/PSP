package server.handlers.actions;

import beans.Request;
import beans.Response;
import beans.User;
import exceptions.WrongMessageFormatException;

public class ConnectionActionHandler implements ActionHandler {
    @Override
    public Response process(Request request) throws WrongMessageFormatException {
        String userParam = User.class.getName().toLowerCase();

        if (!request.getParams().containsKey(userParam)) {
            throw new WrongMessageFormatException(String.format("No param named \"%s\" in connection action", userParam));
        }

        return null;
    }
}

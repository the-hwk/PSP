package handlers;

import data.UDPMessage;

public interface ResponseHandler {
    void handle(UDPMessage response);
}

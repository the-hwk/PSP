package handlers;

import data.AppConfig;
import enums.Action;
import containers.GsonContainer;
import enums.Status;
import data.UDPMessage;

import java.io.IOException;
import java.net.*;

public class ConnectionHandler {
    private static final int PORT = Integer.parseInt(AppConfig.getInstance().getProperty("data_port"));
    private static DatagramSocket socket;
    private static InetSocketAddress address;

    public static boolean createConnection(String host) {
        try {
            socket = new DatagramSocket();

            address = new InetSocketAddress(host, PORT);

            UDPMessage request = new UDPMessage(Action.CONNECTION_OPEN, null);

            UDPMessage response = createRequest(request);

            if (response.getStatus().equals(Status.OK)) {
                return true;
            } else {
                return false;
            }
        } catch (SocketException e) {
            System.out.println(e.getMessage());
            // TODO: Log this
        }

        return false;
    }

    public static UDPMessage createRequest(UDPMessage udpMessage) {
        byte[] buff = udpMessage.toString().getBytes();

        DatagramPacket packet = new DatagramPacket(buff, buff.length, address);
        try {
            socket.send(packet);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        int buffSize = Integer.parseInt(AppConfig.getInstance().getProperty("max_data_buffer_size"));
        packet = new DatagramPacket(new byte[buffSize], buffSize);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return GsonContainer.getGson().fromJson(new String(packet.getData()).trim(), UDPMessage.class);
    }

    public static InetSocketAddress getAddress() {
        return address;
    }
}
package info;

import beans.Action;
import beans.GsonContainer;
import beans.Status;
import beans.UDPMessage;

import java.io.IOException;
import java.net.*;

public class Connection {
    private static final int PORT = 8080;
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

        packet = new DatagramPacket(new byte[10000], 10000);
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
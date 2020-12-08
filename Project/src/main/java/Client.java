import server.enums.Action;
import server.enums.Status;
import server.models.UDPMessage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress("localhost", 8080);
        DatagramSocket socket = new DatagramSocket();

        /*
        UDPMessage request = new UDPMessage(Action.CREATE_NOTIFIER, Status.OK);
        byte[] buff = request.toString().getBytes();
        DatagramPacket packet = new DatagramPacket(buff, buff.length, address);
        socket.send(packet);
        packet = new DatagramPacket(new byte[1024], 1024);
        socket.receive(packet);
        System.out.println(new String(packet.getData()));

        new Scanner(System.in).nextLine();*/

        socket.close();
    }
}

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress("localhost", 8080);
        DatagramSocket socket = new DatagramSocket();

        byte[] buff = "Message".getBytes();

        DatagramPacket packet = new DatagramPacket(buff, buff.length, address);

        socket.send(packet);

        new Scanner(System.in).nextLine();

        socket.close();
    }
}

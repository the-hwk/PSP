package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class Connection {
    private final InetSocketAddress serverAddress;
    private final DatagramSocket socket;

    private Connection(InetSocketAddress serverAddress, DatagramSocket socket) {
        this.serverAddress = serverAddress;
        this.socket = socket;
    }

    public static Connection createConnection(String host, int port) throws SocketException {
        InetSocketAddress serverAddress = new InetSocketAddress(host, port);
        DatagramSocket socket = new DatagramSocket();

        return new Connection(serverAddress, socket);
    }

    public void send(String message) throws IOException {
        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, serverAddress);
        socket.send(packet);
    }

    public String receive() throws IOException {
        DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
        socket.receive(packet);

        return new String(packet.getData());
    }

    public void close() {
        if (!socket.isClosed()) {
            socket.close();
        }
    }
}

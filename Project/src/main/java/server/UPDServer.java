package server;

import server.handlers.UDPServerHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UPDServer {
    public static void main(String[] args) throws IOException {
        if (args.length != 4) {
            System.out.println("Wrong args! Should be <host> <port> <max_threads> <max_message_size>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        int maxThreads = Integer.parseInt(args[2]);
        int buffSize = Integer.parseInt(args[3]);

        InetSocketAddress address = new InetSocketAddress(host, port);
        DatagramSocket socket = new DatagramSocket(address);

        new Thread(new ConsoleThread()).start();

        ExecutorService executor = Executors.newFixedThreadPool(maxThreads);

        System.out.println("The server is running!");

        while (!ServerStatus.isStopped()) {
            DatagramPacket packet = new DatagramPacket(new byte[buffSize], buffSize);
            socket.receive(packet);
            System.out.println("New client: " + packet.getAddress());
            executor.execute(new UDPServerHandler(packet));
        }

        if (!socket.isClosed()) {
            socket.close();
        }

        executor.shutdown();

        System.out.println("Server is down!");
    }

    static class ServerStatus {
        private static boolean stopped = false;

        public static synchronized void setStopped(boolean stopped) {
            ServerStatus.stopped = stopped;
        }

        public static synchronized boolean isStopped() {
            return stopped;
        }
    }
}
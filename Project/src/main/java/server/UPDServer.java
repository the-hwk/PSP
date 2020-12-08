package server;

import server.handlers.UDPNotifier;
import server.handlers.UDPServerHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UPDServer {
    public static void main(String[] args) throws IOException {
        if (args.length != 5) {
            System.out.println("Wrong args! Should be <host> <main_port> <notifier_port> <max_threads> <max_message_size>");
            return;
        }

        String host = args[0];
        int mainPort = Integer.parseInt(args[1]);
        int notifierPort = Integer.parseInt(args[2]);
        int maxThreads = Integer.parseInt(args[3]);
        int buffSize = Integer.parseInt(args[4]);

        DatagramSocket socket = new DatagramSocket(new InetSocketAddress(host, mainPort));
        UDPNotifier.getInstance().init(new InetSocketAddress(host, notifierPort));

        ExecutorService executor = Executors.newFixedThreadPool(maxThreads);

        System.out.println("The server is running!");

        while (!ServerStatus.isStopped()) {
            DatagramPacket packet = new DatagramPacket(new byte[buffSize], buffSize);
            socket.receive(packet);
            System.out.println("Request from client: " + packet.getAddress());
            executor.execute(new UDPServerHandler(socket, packet));
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
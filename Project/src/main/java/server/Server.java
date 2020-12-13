package server;

import server.handlers.NotifierHandler;
import server.handlers.ClientDataHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) throws IOException {
        AppConfig.getInstance().init();

        String host = AppConfig.getInstance().getProperty("host");
        int mainPort = Integer.parseInt(AppConfig.getInstance().getProperty("data_port"));
        int notifierPort = Integer.parseInt(AppConfig.getInstance().getProperty("notifier_port"));
        int maxThreads = Integer.parseInt(AppConfig.getInstance().getProperty("thread_pool_size"));
        int buffSize = Integer.parseInt(AppConfig.getInstance().getProperty("max_data_message_size"));

        DatagramSocket socket = new DatagramSocket(new InetSocketAddress(host, mainPort));
        NotifierHandler.getInstance().init(new InetSocketAddress(host, notifierPort));

        ExecutorService executor = Executors.newFixedThreadPool(maxThreads);

        System.out.println("The server is running!");

        while (!ServerStatus.isStopped()) {
            DatagramPacket packet = new DatagramPacket(new byte[buffSize], buffSize);
            socket.receive(packet);
            System.out.println("Request from client: " + packet.getAddress());
            executor.execute(new ClientDataHandler(socket, packet));
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
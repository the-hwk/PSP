package info;

import beans.*;
import controllers.Controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class NotifyListener implements Runnable {
    private static final int PORT = 8083;
    private static DatagramSocket socket;
    private static InetSocketAddress address;
    private static Controller controller;

    public static void createConnection(String host) {
        try {
            socket = new DatagramSocket();
            address = new InetSocketAddress(host, PORT);
        } catch (SocketException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void setController(Controller c) {
        controller = c;
    }

    @Override
    public void run() {
        while (true) {
            DatagramPacket packet = new DatagramPacket(new byte[10000], 10000);
            try {
                System.out.println("Receive");
                socket.receive(packet);
                System.out.println("Receive READY");

                UDPMessage message = GsonContainer.getGson().fromJson(new String(packet.getData()).trim(), UDPMessage.class);

                System.out.println(message.getBody());

                if (message.getAction().equals(Action.NOTIFY_MESSAGE)) {
                    controller.notify(GsonContainer.getGson().fromJson(message.getBody(), MessageEntity.class));
                } else {
                    controller.notify(GsonContainer.getGson().fromJson(message.getBody(), RoomEntity.class));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

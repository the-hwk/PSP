package server.handlers;

import java.net.DatagramPacket;

public class UDPServerHandler implements Runnable {
    private final DatagramPacket packet;

    public UDPServerHandler(DatagramPacket packet) {
        this.packet = packet;
    }

    @Override
    public void run() {
        System.out.println(new String(packet.getData()));
    }
}

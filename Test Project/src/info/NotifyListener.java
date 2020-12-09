package info;

import beans.*;
import controllers.Controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class NotifyListener implements Runnable {
    private static final int PORT = 8083;

    private InetSocketAddress serverAddress;
    private DatagramChannel channel;
    private final Controller controller;

    public NotifyListener(String host, Controller controller) {
        try {
            channel = DatagramChannel.open();
            channel.socket().bind(new InetSocketAddress(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

        serverAddress = new InetSocketAddress(host, PORT);
        this.controller = controller;
    }

    @Override
    public void run() {
        while (true) {
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            buffer.clear();

            try {
                if (channel.receive(buffer) != null) {
                    buffer.flip();

                    byte[] bytes = new byte[buffer.limit()];
                    buffer.get(bytes, 0, buffer.limit());

                    UDPMessage message = GsonContainer.getGson().fromJson(new String(bytes).trim(), UDPMessage.class);

                    if (message.getAction().equals(Action.NOTIFY_MESSAGE)) {
                        controller.notify(GsonContainer.getGson().fromJson(message.getBody(), MessageEntity.class));
                    } else {
                        controller.notify(GsonContainer.getGson().fromJson(message.getBody(), RoomEntity.class));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getHost() {
        return serverAddress.getHostName();
    }

    public int getPort() {
        return channel.socket().getLocalPort();
    }
}

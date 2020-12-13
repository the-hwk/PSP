package handlers;

import beans.*;
import containers.GsonContainer;
import controllers.Controller;
import data.AppConfig;
import data.UDPMessage;
import enums.Action;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class NotifyHandler implements Runnable {
    private static final int BUFFER = Integer.parseInt(AppConfig.getInstance().getProperty("max_notify_buffer_size"));

    private DatagramChannel channel;
    private final Controller controller;

    public NotifyHandler (Controller controller) {
        try {
            channel = DatagramChannel.open();
            channel.socket().bind(new InetSocketAddress(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.controller = controller;
    }

    @Override
    public void run() {
        while (true) {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER);
            buffer.clear();

            try {
                if (channel.receive(buffer) != null) {
                    buffer.flip();

                    byte[] bytes = new byte[buffer.limit()];
                    buffer.get(bytes, 0, buffer.limit());

                    UDPMessage message = GsonContainer.getGson().fromJson(new String(bytes).trim(), UDPMessage.class);

                    if (message.getAction().equals(Action.NOTIFY_MESSAGE)) {
                        controller.notify(GsonContainer.getGson().fromJson(message.getBody(), MessageEntity.class));
                    } else if (message.getAction().equals(Action.NOTIFY_ROOM)) {
                        controller.notify(GsonContainer.getGson().fromJson(message.getBody(), RoomEntity.class));
                    } else if (message.getAction().equals(Action.CLOSE_NOTIFIER)) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getPort() {
        return channel.socket().getLocalPort();
    }
}
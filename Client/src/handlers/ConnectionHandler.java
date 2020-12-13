package handlers;

import containers.GsonContainer;
import data.AppConfig;
import data.UDPMessage;
import enums.Action;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Позволяет устанавливать соединение с сервером и отправлять сообщения
 */
public class ConnectionHandler {
    private static final int PORT = Integer.parseInt(
            AppConfig.getInstance().getProperty("data_port")
    );
    private static DatagramSocket socket;
    private static InetSocketAddress address;

    private static boolean isReceive = false;

    private static final Logger logger = Logger.getLogger(ConnectionHandler.class);

    /**
     * Создаёт соединение с сервером
     * @param host Хост сервера
     * @param handler Обработчик результата
     */
    public static void createConnection(String host, ResponseHandler handler) {
        if (isReceive) {
            return;
        }
        new Thread(() -> handler.handle(createConnection(host))).start();
    }

    /**
     * Создаёт соединение с сервером
     * @param host Хост сервера
     * @return Ответ сервера в случае успешного соединения
     * или null в случае ошибки соединения
     */
    public static UDPMessage createConnection(String host) {
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(Integer.parseInt(AppConfig.getInstance().getProperty("udp_receive_timeout")));

            address = new InetSocketAddress(host, PORT);

            UDPMessage request = new UDPMessage(Action.CONNECTION_OPEN, null);
            return createRequest(request);

        } catch (SocketException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    /**
     * Отправляет запрос и принимает ответ от сервера
     * @param udpMessage Запрос
     * @param handler Обработчик ответа от сервера
     */
    public static void createRequest(UDPMessage udpMessage, ResponseHandler handler) {
        if (isReceive) {
            return;
        }
        new Thread(() -> handler.handle(createRequest(udpMessage))).start();
    }

    /**
     * Отправляет запрос и принимает ответ от сервера
     * @param udpMessage Запрос
     * @return Ответ сервера или null в случае ошибки соединения
     */
    public static UDPMessage createRequest(UDPMessage udpMessage) {
        byte[] buff = udpMessage.toString().getBytes();

        DatagramPacket packet = new DatagramPacket(buff, buff.length, address);

        // Отправка сообщения
        try {
            socket.send(packet);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        int buffSize = Integer.parseInt(AppConfig.getInstance().getProperty("max_data_buffer_size"));
        packet = new DatagramPacket(new byte[buffSize], buffSize);

        // Получение ответа
        try {
            isReceive = true;
            socket.receive(packet);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            isReceive = false;
        }

        return GsonContainer.getGson().fromJson(new String(packet.getData()).trim(), UDPMessage.class);
    }
}
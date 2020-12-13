package controllers;

import beans.MessageEntity;
import beans.PasswordEntity;
import beans.RoomEntity;
import beans.UserEntity;
import com.google.gson.reflect.TypeToken;
import containers.GsonContainer;
import data.Data;
import data.UDPMessage;
import enums.Action;
import enums.Status;
import handlers.ConnectionHandler;
import handlers.NotifyHandler;
import javafx.application.Platform;
import netscape.javascript.JSObject;
import sample.KeyEventContainer;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 * Основной контроллер приложения.
 * Содержит методы, которые вызываются из JS кода main.html.
 * Все методы производят обмен сообщениями с сервером
 */
public class Controller {
    private final JSObject windowObject;

    public Controller(JSObject windowObject) {
        this.windowObject = windowObject;
    }

    /**
     * Производит попытку установления соединения с сервером
     * @param host Адрес хоста сервера
     */
    public void createConnection(String host) {
        ConnectionHandler.createConnection(host, (response -> {
            Platform.runLater(() -> {
                if (response != null && response.getStatus().equals(Status.OK)) {
                    windowObject.call("showPage", "login");
                } else {
                    windowObject.call("showError", "Не удалось подключиться к серверу");
                }
            });
        }));
    }

    /**
     * Выполнение входа в аккаунт пользователя
     * @param email Email
     * @param passwordStr Пароль
     */
    public void login(String email, String passwordStr) {
        UserEntity user = new UserEntity();
        user.setEmail(email);

        PasswordEntity password = new PasswordEntity();
        password.setUser(user);
        password.setValue(passwordStr.getBytes());

        UDPMessage request = new UDPMessage(Action.LOGIN, GsonContainer.getGson().toJson(password));

        ConnectionHandler.createRequest(request, (response) -> {
            Platform.runLater(() -> {
                if (response != null) {
                    if (response.getStatus().equals(Status.OK)) {
                        windowObject.call("showInfo", "Вы вошли в свой аккаунт");
                        windowObject.call("showPage", "main");
                        windowObject.call("setUser", response.getBody());
                        windowObject.call("renderRooms");

                        Data.setUser(GsonContainer.getGson().fromJson(response.getBody(), UserEntity.class));
                        createNotifier();
                    } else if (response.getStatus().equals(Status.USER_LOGGED)) {
                        windowObject.call("showError", "Такой пользователь уже в системе");
                    } else if (response.getStatus().equals(Status.NOT_FOUND)) {
                        windowObject.call("showError", "Аккаунт не найден");
                    } else {
                        windowObject.call("showError", "Ошибка на сервере");
                    }
                } else {
                    windowObject.call("showError", "Не удалось получить ответ от сервера");
                }
            });
        });
    }

    /**
     * Инициализация и запуск "Центра уведомлений"
     */
    public void createNotifier() {
        NotifyHandler listener = new NotifyHandler(this);

        String message = GsonContainer.getGson().toJson(Data.getUser()) + "\n" + listener.getPort();

        UDPMessage request = new UDPMessage(Action.CREATE_NOTIFIER, message);

        ConnectionHandler.createRequest(request, (response -> {
            Platform.runLater(() -> {
                if (response != null) {
                    if (response.getStatus().equals(Status.OK)) {
                        Data.setNotifierThread(new Thread(listener));
                        Data.getNotifierThread().start();
                    } else {
                        windowObject.call("showError", "Не удалось создать центр уведомлений");
                    }
                } else {
                    windowObject.call("showError", "Не удалось получить ответ от сервера");
                }
            });
        }));
    }

    /**
     * Выполняет регистрацию нового пользователя
     * @param email Email
     * @param name Имя пользователя
     * @param passwordStr Пароль
     */
    public void registration(String email, String name, String passwordStr) {
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setNickname(name);

        PasswordEntity password = new PasswordEntity();
        password.setUser(user);
        password.setValue(passwordStr.getBytes());

        UDPMessage request = new UDPMessage(Action.REGISTRATION, GsonContainer.getGson().toJson(password));

        ConnectionHandler.createRequest(request, (response -> {
            Platform.runLater(() -> {
                if (response != null) {
                    if (response.getStatus().equals(Status.OK)) {
                        windowObject.call("showInfo", "Вы зарегистрированы");
                        windowObject.call("showPage", "login");
                    } else if (response.getStatus().equals(Status.USER_DUPLICATE)) {
                        windowObject.call("showError", "Такой пользователь уже существует");
                    } else {
                        windowObject.call("showError", "Ошибка на сервере");
                    }
                } else {
                    windowObject.call("showError", "Не удалось получить ответ от сервера");
                }
            });
        }));
    }

    /**
     * Получает список пользователей
     */
    public void getUsers() {
        UDPMessage request = new UDPMessage(Action.GET_USERS, null);

        ConnectionHandler.createRequest(request, (response -> {
            Platform.runLater(() -> {
                if (response != null) {
                    if (response.getStatus().equals(Status.OK)) {
                        windowObject.call("fillUsersSelect", response.getBody());
                    } else {
                        windowObject.call("showError", "Не удалось получить данные о пользователях");
                    }
                } else {
                    windowObject.call("showError", "Не удалось получить ответ от сервера");
                }
            });

        }));
    }

    /**
     * Получает список сообщений для заданного чата
     * @param roomId Id чата
     */
    public void getMessages(int roomId) {
        RoomEntity room = new RoomEntity();
        room.setId(roomId);

        UDPMessage request = new UDPMessage(Action.GET_MESSAGES, GsonContainer.getGson().toJson(room));

        ConnectionHandler.createRequest(request, (response -> {
            Platform.runLater(() -> {
                if (response != null) {
                    if (response.getStatus().equals(Status.OK)) {
                        windowObject.call("appRenderMessages", response.getBody());
                    } else {
                        windowObject.call("showError", "Не удалось получить список сообщений");
                    }
                } else {
                    windowObject.call("showError", "Не удалось получить ответ от сервера");
                }
            });
        }));
    }

    /**
     * Выполняет добавление нового чата
     * @param name Название чата
     * @param usersIdJson Строковое представление списка пользователей
     *                    (UserEntity) в формате JSON
     */
    public void createRoom(String name, String usersIdJson) {
        Set<UserEntity> users = GsonContainer.getGson().fromJson(usersIdJson, new TypeToken<Set<UserEntity>>() {}.getType());

        RoomEntity room = new RoomEntity();
        room.setName(name); 
        room.setUsers(users);

        UDPMessage request = new UDPMessage(Action.ADD_ROOM, GsonContainer.getGson().toJson(room));

        ConnectionHandler.createRequest(request, (response -> {
            Platform.runLater(() -> {
                if (response != null) {
                    if (response.getStatus().equals(Status.OK)) {
                        windowObject.call("showInfo", "Чат добавлен");
                        windowObject.call("showPage", "main");
                    } else {
                        windowObject.call("showError", "Не удалось добавить чат");
                    }
                } else {
                    windowObject.call("showError", "Не удалось получить ответ от сервера");
                }
            });
        }));
    }

    /**
     * Выполняет отправление сообщения в указанный чат
     * @param userId Id пользователя
     * @param roomId Id чата
     * @param value Текст сообщения
     */
    public void createMessage(int userId, int roomId, String value) {
        UserEntity user = new UserEntity();
        user.setId(userId);

        RoomEntity room = new RoomEntity();
        room.setId(roomId);

        MessageEntity message = new MessageEntity();
        message.setDateVal(new Timestamp(new Date().getTime()));
        message.setFromUser(user);
        message.setValue(value);
        message.setToRoom(room);

        UDPMessage request = new UDPMessage(Action.ADD_MESSAGE, GsonContainer.getGson().toJson(message));

        ConnectionHandler.createRequest(request, (response -> {
            Platform.runLater(() -> {
                if (response != null) {
                    if (!response.getStatus().equals(Status.OK)) {
                        windowObject.call("showError", "Ошибка отправки сообщения");
                    }
                } else {
                    windowObject.call("showError", "Не удалось получить ответ от сервера");
                }
            });
        }));
    }

    /**
     * Вызывается из метода run класса NotifyHandler.
     * Принимает уведомление о новом сообщении
     * @param message Сообщение
     */
    public void notify(MessageEntity message) {
        Platform.runLater(() -> {
            windowObject.call("notifyMessage", GsonContainer.getGson().toJson(message));
        });
    }

    /**
     * Вызывается из метода run класса NotifyHandler.
     * Принимает уведомление о новом чате
     * @param room Чат
     */
    public void notify(RoomEntity room) {
        Platform.runLater(() -> {
            windowObject.call("addRoomFromJson", GsonContainer.getGson().toJson(room));
            windowObject.call("notifyRoom", GsonContainer.getGson().toJson(room));
        });
    }

    public void setKeyEventOnBtn(String btnId) {
        KeyEventContainer.setBtnId(btnId);
    }
}
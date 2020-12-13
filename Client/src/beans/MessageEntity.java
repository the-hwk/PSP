package beans;

import java.sql.Timestamp;

public class MessageEntity {
    private int id;
    private String value;
    private Timestamp dateVal;
    private UserEntity fromUser;
    private RoomEntity toRoom;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Timestamp getDateVal() {
        return dateVal;
    }

    public void setDateVal(Timestamp dateVal) {
        this.dateVal = dateVal;
    }

    public UserEntity getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserEntity fromUser) {
        this.fromUser = fromUser;
    }

    public RoomEntity getToRoom() {
        return toRoom;
    }

    public void setToRoom(RoomEntity toRoom) {
        this.toRoom = toRoom;
    }
}
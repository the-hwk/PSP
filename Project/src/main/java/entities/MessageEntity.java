package entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "messages", schema = "multichat")
public class MessageEntity {
    private int id;
    private String value;
    private Timestamp dateVal;
    private UserEntity fromUser;
    private RoomEntity toRoom;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "value", nullable = false, length = 300)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Basic
    @Column(name = "dateVal", nullable = false)
    public Timestamp getDateVal() {
        return dateVal;
    }

    public void setDateVal(Timestamp dateVal) {
        this.dateVal = dateVal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageEntity that = (MessageEntity) o;

        if (id != that.id) return false;
        if (!Objects.equals(value, that.value)) return false;
        return Objects.equals(dateVal, that.dateVal);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public UserEntity getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserEntity usersByUserId) {
        this.fromUser = usersByUserId;
    }

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id", nullable = false)
    public RoomEntity getToRoom() {
        return toRoom;
    }

    public void setToRoom(RoomEntity roomsByRoomId) {
        this.toRoom = roomsByRoomId;
    }
}

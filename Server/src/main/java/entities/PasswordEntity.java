package entities;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "passwords", schema = "multichat")
public class PasswordEntity {
    private int id;
    private byte[] value;
    private UserEntity user;

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
    @Column(name = "value", nullable = false)
    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PasswordEntity that = (PasswordEntity) o;

        if (id != that.id) return false;
        return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity usersByUserId) {
        this.user = usersByUserId;
    }
}

package entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "rooms", schema = "multichat")
public class RoomEntity {
    private int id;
    private String name;
    private Set<UserEntity> users = new HashSet<>();

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
    @Column(name = "name", nullable = false, length = 30)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "rooms", cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> details) {
        this.users = details;
    }

    public void addUser(UserEntity detail) {
        users.add(detail);
    }

    public void removeUser(UserEntity detail) {
        users.remove(detail);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomEntity that = (RoomEntity) o;

        if (id != that.id) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }
}

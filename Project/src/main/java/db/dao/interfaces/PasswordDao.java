package db.dao.interfaces;

import beans.Password;
import beans.User;

import java.util.Optional;

public interface PasswordDao extends CrudOperations<Password> {
    public Optional<Password> selectByUser(User user);
}

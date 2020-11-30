package db.dao.interfaces;

import beans.User;

import java.util.Optional;

public interface UserDao extends CrudOperations<User> {
    Optional<User> selectByEmail(String email);
}

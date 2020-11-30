package db.dao.mysql;

import beans.User;
import db.connection.ConnectionPool;
import db.dao.CallableStatementParametersSetter;
import db.dao.interfaces.UserDao;
import org.apache.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import tools.ResourceManager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MySqlUserDao extends CrudHandler<User> implements UserDao {
    private static final String FIELD_ID = "id";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_NICKNAME = "nickname";
    private static final String FIELD_ROLE = "role";

    private static final String CALL_SELECT_BY_ID = "Users.SelectById";
    private static final String CALL_SELECT_BY_EMAIL = "Users.SelectByEmail";
    private static final String CALL_SELECT_ALL = "Users.SelectAll";
    private static final String CALL_INSERT = "Users.Insert";

    private final ResourceManager resource = new ResourceManager("mysql");

    private final Logger logger = Logger.getLogger(MySqlUserDao.class);

    @Override
    public Optional<User> selectByEmail(String email) {
        try {
            Connection connection = ConnectionPool.getInstance().getConnection();
            CallableStatement callState = connection.prepareCall(resource.getProperty(CALL_SELECT_BY_EMAIL));
            callState.setString(1, email);

            ResultSet rs = callState.executeQuery();

            if (rs.next()) {
                return getFields(rs);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public CallableStatementParametersSetter getCallStateParamSetterForInsert(User obj) {
        return callState -> {
            callState.setString(1, obj.getEmail());
            callState.setString(2, obj.getNickname());
            callState.setInt(3, obj.getRole().getId());
        };
    }

    @Override
    public CallableStatementParametersSetter getCallStateParamSetterForUpdate(User obj) {
        throw new NotImplementedException();
    }

    @Override
    public CallableStatementParametersSetter getCallStateParamSetterForDelete(User obj) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<User> getFields(ResultSet rs) throws SQLException {
        int id = rs.getInt(FIELD_ID);
        String email = rs.getString(FIELD_EMAIL);
        String name = rs.getString(FIELD_NICKNAME);
        Optional<User.Role> role = User.Role.fromInt(rs.getInt(FIELD_ROLE));

        return role.map(userRole -> new User(id, email, name, userRole));
    }

    @Override
    public String getCallSelectById() {
        return resource.getProperty(CALL_SELECT_BY_ID);
    }

    @Override
    public String getCallSelectAll() {
        return resource.getProperty(CALL_SELECT_ALL);
    }

    @Override
    public String getCallInsert() {
        return resource.getProperty(CALL_INSERT);
    }

    @Override
    public String getCallUpdate() {
        throw new NotImplementedException();
    }

    @Override
    public String getCallDelete() {
        throw new NotImplementedException();
    }

    @Override
    public Logger getLogger() {
        throw new NotImplementedException();
    }
}

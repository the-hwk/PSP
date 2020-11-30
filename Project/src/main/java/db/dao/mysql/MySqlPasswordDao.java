package db.dao.mysql;

import beans.Password;
import beans.User;
import db.connection.ConnectionPool;
import db.dao.CallableStatementParametersSetter;
import db.dao.DaoFactory;
import db.dao.interfaces.PasswordDao;
import org.apache.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import tools.ResourceManager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MySqlPasswordDao extends CrudHandler<Password> implements PasswordDao {
    private static final String FIELD_ID = "id";
    private static final String FIELD_USER = "userId";
    private static final String FIELD_PASSWORD = "value";

    private static final String CALL_SELECT_BY_USER = "Passwords.SelectByUser";
    private static final String CALL_INSERT = "Passwords.Insert";

    private final ResourceManager resource = new ResourceManager("mysql");

    private final Logger logger = Logger.getLogger(MySqlPasswordDao.class);

    @Override
    public Optional<Password> selectByUser(User user) {
        try {
            Connection connection = ConnectionPool.getInstance().getConnection();
            CallableStatement callState = connection.prepareCall(resource.getProperty(CALL_SELECT_BY_USER));
            callState.setInt(1, user.getId());

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
    public CallableStatementParametersSetter getCallStateParamSetterForInsert(Password obj) {
        return callState -> {
            callState.setInt(1, obj.getUser().getId());
            callState.setBytes(2, obj.getValue());
        };
    }

    @Override
    public CallableStatementParametersSetter getCallStateParamSetterForUpdate(Password obj) {
        throw new NotImplementedException();
    }

    @Override
    public CallableStatementParametersSetter getCallStateParamSetterForDelete(Password obj) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<Password> getFields(ResultSet rs) throws SQLException {
        int id = rs.getInt(FIELD_ID);
        Optional<User> user = DaoFactory.getDaoFactory(DaoFactory.Consumer.MYSQL).getUserDao()
                .select(rs.getInt(FIELD_USER));
        byte[] password = rs.getBytes(FIELD_PASSWORD);

        return user.map(userData -> new Password(id, userData, password));
    }

    @Override
    public String getCallSelectById() {
        throw new NotImplementedException();
    }

    @Override
    public String getCallSelectAll() {
        throw new NotImplementedException();
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
        return logger;
    }
}

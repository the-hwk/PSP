package db.dao.mysql;

import beans.Room;
import beans.User;
import db.connection.ConnectionPool;
import db.dao.CallableStatementParametersSetter;
import db.dao.interfaces.RoomDao;
import org.apache.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import tools.ResourceManager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlRoomDao extends CrudHandler<Room> implements RoomDao {
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";

    private static final String CALL_SELECT_BY_ID = "Rooms.SelectById";
    private static final String CALL_SELECT_BY_USER = "Rooms.SelectByUser";
    private static final String CALL_INSERT = "Rooms.Insert";

    private final ResourceManager resource = new ResourceManager("mysql");

    private final Logger logger = Logger.getLogger(MySqlRoomDao.class);

    @Override
    public List<Room> selectByUser(User user) {
        List<Room> rooms = new ArrayList<>();

        try {
            Connection connection = ConnectionPool.getInstance().getConnection();
            CallableStatement callState = connection.prepareCall(resource.getProperty(CALL_SELECT_BY_USER));
            callState.setInt(1, user.getId());

            ResultSet rs = callState.executeQuery();

            while (rs.next()) {
                getFields(rs).ifPresent(rooms::add);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return rooms;
    }

    @Override
    public CallableStatementParametersSetter getCallStateParamSetterForInsert(Room obj) {
        return callState -> {
            callState.setString(1, obj.getName());
        };
    }

    @Override
    public CallableStatementParametersSetter getCallStateParamSetterForUpdate(Room obj) {
        throw new NotImplementedException();
    }

    @Override
    public CallableStatementParametersSetter getCallStateParamSetterForDelete(Room obj) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<Room> getFields(ResultSet rs) throws SQLException {
        int id = rs.getInt(FIELD_ID);
        String name = rs.getString(FIELD_NAME);

        return Optional.of(new Room(id, name));
    }

    @Override
    public String getCallSelectById() {
        return resource.getProperty(CALL_SELECT_BY_ID);
    }

    @Override
    public String getCallSelectAll() {
        return null;
    }

    @Override
    public String getCallInsert() {
        return resource.getProperty(CALL_INSERT);
    }

    @Override
    public String getCallUpdate() {
        return null;
    }

    @Override
    public String getCallDelete() {
        return null;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}

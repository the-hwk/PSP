package db.dao.mysql;

import beans.Message;
import beans.Room;
import beans.User;
import db.connection.ConnectionPool;
import db.dao.CallableStatementParametersSetter;
import db.dao.DaoFactory;
import db.dao.interfaces.MessageDao;
import org.apache.log4j.Logger;
import tools.ResourceManager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlMessageDao extends CrudHandler<Message> implements MessageDao {
    private static final String FIELD_ID = "id";
    private static final String FIELD_VALUE = "value";
    private static final String FIELD_FROM = "from";
    private static final String FIELD_TO = "to";

    private static final String CALL_SELECT_BY_ID = "Messages.SelectById";
    private static final String CALL_SELECT_BY_ROOM = "Messages.SelectByRoom";
    private static final String CALL_INSERT = "Messages.Insert";

    private final ResourceManager resource = new ResourceManager("mysql");

    private final Logger logger = Logger.getLogger(MySqlMessageDao.class);

    @Override
    public List<Message> selectByRoom(Room room) {
        List<Message> messages = new ArrayList<>();

        try {
            Connection connection = ConnectionPool.getInstance().getConnection();
            CallableStatement callState = connection.prepareCall(resource.getProperty(CALL_SELECT_BY_ROOM));
            callState.setInt(1, room.getId());

            ResultSet rs = callState.executeQuery();

            while (rs.next()) {
                getFields(rs).ifPresent(messages::add);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return messages;
    }

    @Override
    public CallableStatementParametersSetter getCallStateParamSetterForInsert(Message obj) {
        return callState -> {
            callState.setString(1, obj.getValue());
            callState.setInt(2, obj.getFrom().getId());
            callState.setInt(3, obj.getTo().getId());
        };
    }

    @Override
    public CallableStatementParametersSetter getCallStateParamSetterForUpdate(Message obj) {
        return null;
    }

    @Override
    public CallableStatementParametersSetter getCallStateParamSetterForDelete(Message obj) {
        return null;
    }

    @Override
    public Optional<Message> getFields(ResultSet rs) throws SQLException {
        int id = rs.getInt(FIELD_ID);
        String value = rs.getString(FIELD_VALUE);
        Optional<User> user = DaoFactory.getDaoFactory(DaoFactory.Consumer.MYSQL).getUserDao()
                .select(rs.getInt(FIELD_FROM));
        Optional<Room> room = DaoFactory.getDaoFactory(DaoFactory.Consumer.MYSQL).getRoomDao()
                .select(rs.getInt(FIELD_TO));

        if (user.isPresent() && room.isPresent()) {
            return Optional.of(new Message(id, value, user.get(), room.get()));
        }

        return Optional.empty();
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

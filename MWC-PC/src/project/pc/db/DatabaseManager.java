package project.pc.db;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.MySQLConnection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

    private MysqlDataSource mSource;
    private static final String sDB_NAME = "election";
    private static final String sSERVER_NAME = "localhost";
    private static final String sUSER_NAME = "sudipto";
    private static final String sTABLE_NAME = "VOTES";
    private Connection mConnection;
    private PreparedStatement mInsertStatement;
    private PreparedStatement mCountStatement;
    private static final String sINS_QUERY = "insert into " + sDB_NAME + "." + sTABLE_NAME +
                                                " values(?,?)";
    private static final String sSEL_QUERY = "select count(*) as vcount from " + sDB_NAME + "." + sTABLE_NAME + " as T " +
                                                "where T.OPTION = ?";
    public DatabaseManager() {
        mSource = new MysqlDataSource();
        mSource.setDatabaseName(sDB_NAME);
        mSource.setServerName(sSERVER_NAME);
        mSource.setUser(sUSER_NAME);

        mConnection = null;
        mInsertStatement = null;
        mCountStatement = null;
        try {
            mConnection = (MySQLConnection) mSource.getConnection();
            if (mConnection != null) {
                mInsertStatement = (PreparedStatement) mConnection.prepareStatement(sINS_QUERY);
                mCountStatement = (PreparedStatement) mConnection.prepareStatement(sSEL_QUERY);
            }
        } catch (SQLException e) {
            ////TODO
        }
    }

    public void addToDatabase(String address, String option) {
        try {
            mInsertStatement.setString(1, address);
            mInsertStatement.setString(2, option);
            int n = mInsertStatement.executeUpdate();
        } catch (SQLException e) {}
    }

    public String getCurrentVoteCount() {
        StringBuilder reply = new StringBuilder();
        try {
            for(int i=1;i<=4;++i) {
                mCountStatement.setString(1, i+"");
                ResultSet rs = mCountStatement.executeQuery();
                rs.next();
                reply.append(rs.getLong("vcount") + "\n");
            }
        } catch (SQLException e) {}
        return reply.toString();
    }
}

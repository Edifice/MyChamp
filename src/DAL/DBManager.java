package DAL;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This is an abstract class which contains the connection to the database so
 * all classes extending this class, will get the connection to the database and
 * also has to implement the abstract methods.
 *
 * @author Martin
 */
public abstract class DBManager {

    protected Connection con;
    protected SQLServerDataSource dS;

    /**
     * Creates the connection to the database
     *
     * @throws SQLException
     */
    public DBManager() throws SQLException {
        dS = new SQLServerDataSource();
        dS.setUser("java");
        dS.setPassword("java");
        dS.setServerName("localhost");
        dS.setDatabaseName("MyChamp");
        dS.setInstanceName("SQLEXPRESS");
        con = dS.getConnection();
        con.close();
    }

    public abstract void removeById(int iden) throws SQLException;

    public abstract ArrayList getAll() throws SQLException;

    public abstract void removeAll() throws SQLException;
}

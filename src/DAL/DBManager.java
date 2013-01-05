package DAL;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class DBManager {
    protected Connection con;
    protected SQLServerDataSource dS;
    
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

}

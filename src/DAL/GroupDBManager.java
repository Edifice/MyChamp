package DAL;

import BE.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GroupDBManager extends DBManager {
    
    public GroupDBManager() throws SQLException {
        super();
    }
    
    public void addGroup(Group group) throws SQLException {
        Connection con = dS.getConnection();
        PreparedStatement qGroup = con.prepareStatement("INSERT INTO Groups VALUES (?)");
        
        qGroup.setString(1, group.getGroupName());
        qGroup.executeUpdate();
        
        con.close();
    }
    
    public void updateGroup(Group group) throws SQLException {
        Connection con = dS.getConnection();
        
        PreparedStatement qGroup = con.prepareStatement("UPDATE Groups SET GroupName = ? WHERE ID = ?");
        
        qGroup.setString(1, group.getGroupName());
        
        qGroup.setInt(4, group.getID());
        
        qGroup.executeUpdate();
        
        con.close();
    }
    
    @Override
    public void removeById(int iden) throws SQLException {
        Connection con = dS.getConnection();
        PreparedStatement qTeam = con.prepareStatement("DELETE FROM Groups WHERE ID = ?");
        qTeam.setInt(1, iden);
        
        qTeam.executeUpdate();
        
        con.close();
    }
    
    @Override
    public ArrayList getAll() throws SQLException {
        Connection con = dS.getConnection();
        ArrayList<Group> groups = new ArrayList<>();
        
        PreparedStatement qAllGroups = con.prepareStatement("SELECT * FROM Groups");
        
        ResultSet allTeams = qAllGroups.executeQuery();
        
        while (allTeams.next()) {
            groups.add(
                    new Group(
                    allTeams.getInt("ID"),
                    allTeams.getString("GroupName")));
            
        }
        
        con.close();
        return groups;
    }
    
    public Group getGroupById(int id) throws SQLException {
        Connection con = dS.getConnection();
        ArrayList<Group> groups = new ArrayList<>();
        
        PreparedStatement qAllGroups = con.prepareStatement("SELECT * FROM Groups WHERE ID = ?");
        qAllGroups.setInt(1, id);
        ResultSet allTeams = qAllGroups.executeQuery();
        
        allTeams.next();
        Group group = new Group(
                    allTeams.getInt("ID"),
                    allTeams.getString("GroupName"));
        con.close();
        return group;
    }
    
    @Override
    public void removeAll() throws SQLException {
        Connection con = dS.getConnection();
        PreparedStatement qData = con.prepareStatement("DELETE FROM Groups; DBCC CHECKIDENT (Groups, RESEED, 0)");
        qData.executeUpdate();
        
        con.close();
    }
}

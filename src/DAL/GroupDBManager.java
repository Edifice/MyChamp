package DAL;

import BE.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class extends DBManager, and takes care of all the handling of the Group
 * table in the database.
 *
 * @author Martin
 */
public class GroupDBManager extends DBManager {

    public GroupDBManager() throws SQLException {
        super();
    }

    /**
     * This method adds a group entity's values into the Group table of the
     * database. Be aware that the groups ID is auto-generated.
     *
     * @param group is the group entity.
     * @throws SQLException
     */
    public void addGroup(Group group) throws SQLException {
        Connection con = dS.getConnection();
        PreparedStatement qGroup = con.prepareStatement("INSERT INTO Groups VALUES (?)");

        qGroup.setString(1, group.getGroupName());
        qGroup.executeUpdate();

        con.close();
    }

    /**
     * This method updates an existing group in the Group table of the database,
     * with the new entity's values. Be aware that the groups ID is
     * auto-generated.
     *
     * @param group is the group entity
     * @throws SQLException
     */
    public void updateGroup(Group group) throws SQLException {
        Connection con = dS.getConnection();

        PreparedStatement qGroup = con.prepareStatement("UPDATE Groups SET GroupName = ? WHERE ID = ?");

        qGroup.setString(1, group.getGroupName());

        qGroup.setInt(4, group.getID());

        qGroup.executeUpdate();

        con.close();
    }

    /**
     * This method is an abstract method; It removes a group from the database,
     * matching the id.
     *
     * @param iden is the id.
     * @throws SQLException
     */
    @Override
    public void removeById(int iden) throws SQLException {
        Connection con = dS.getConnection();
        PreparedStatement qTeam = con.prepareStatement("DELETE FROM Groups WHERE ID = ?");
        qTeam.setInt(1, iden);

        qTeam.executeUpdate();

        con.close();
    }

    /**
     * This method is an abstract method; It gets all the groups from the
     * database, and adds them to an ArrayList.
     *
     * @return the ArrayList containing all the teams.
     * @throws SQLException
     */
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

    /**
     * This method gets a specific group from the database, that matches the id
     *
     * @param id is the id
     * @return a Group entity.
     * @throws SQLException
     */
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

    /**
     * This method is an abstract method; It removes all groups from the
     * database, and resets the Identity to 0;
     *
     * @throws SQLException
     */
    @Override
    public void removeAll() throws SQLException {
        Connection con = dS.getConnection();
        PreparedStatement qData = con.prepareStatement("DELETE FROM Groups; DBCC CHECKIDENT (Groups, RESEED, 0)");
        qData.executeUpdate();

        con.close();
    }

    /**
     * This method get the group names of the groups in the database.
     *
     * @return an ArrayList containing the names of the groups.
     * @throws SQLException
     */
    public ArrayList<String> getGroupNames() throws SQLException {
        Connection con = dS.getConnection();
        ArrayList<String> ret = new ArrayList<>();

        PreparedStatement qAllGroups = con.prepareStatement("SELECT GroupName FROM Groups ORDER BY ID");
        ResultSet allGroups = qAllGroups.executeQuery();

        while (allGroups.next()) {
            ret.add(allGroups.getString("GroupName"));
        }

        con.close();
        return ret;
    }
}

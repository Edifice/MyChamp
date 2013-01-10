package DAL;

import BE.Group;
import BE.Team;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TeamDBManager extends DBManager {

    public TeamDBManager() throws SQLException {
        super();
    }
    
    /**
     * This method is used for adding a team to the database.
     * be aware that you won't be signing the id, groupID or the points. 
     * @param team is an instance of the BE.Team.
     * 
     * @throws SQLException 
     */
    public void addTeam(Team team) throws SQLException {
        Connection con = dS.getConnection();
        PreparedStatement qTeam = con.prepareStatement("INSERT INTO Team VALUES (?, ?, ?, ?, ?)");

        
        qTeam.setString(1, team.getSchool());
        qTeam.setString(2, team.getTeamCaptain());
        qTeam.setString(3, team.getEmail());
        qTeam.setNull(4, java.sql.Types.INTEGER);
        qTeam.setNull(5, java.sql.Types.INTEGER);

        qTeam.executeUpdate();

        con.close();
    }
    /**
     * This method is used for updating several columns within the Team table of the database.
     * Be aware this method can not be used for updating the id, group or points of the team.
     * 
     * @param team is an instance of the BE.Team.
     * 
     * @throws SQLException 
     */
    public void updateTeam(Team team) throws SQLException {
        Connection con = dS.getConnection();

        PreparedStatement qTeam = con.prepareStatement("UPDATE Team SET  School = ?, TeamCaptain = ?, Email = ? WHERE ID = ?");

        qTeam.setString(1, team.getSchool());
        qTeam.setString(2, team.getTeamCaptain());
        qTeam.setString(3, team.getEmail());

        qTeam.setInt(4, team.getID());

        qTeam.executeUpdate();

        con.close();
    }

    @Override
    public void removeById(int id) throws SQLException {
        Connection con = dS.getConnection();
        PreparedStatement qTeam = con.prepareStatement("DELETE FROM Team WHERE ID = ?");
        qTeam.setInt(1, id);

        qTeam.executeUpdate();

        con.close();
    }

    ;
    public ArrayList<Team> getAllWithGroupNames() throws SQLException {
        Connection con = dS.getConnection();
        ArrayList<Team> teams = new ArrayList<>();

        PreparedStatement qAllTeams = con.prepareStatement("SELECT Team.*, Groups.GroupName FROM Team LEFT JOIN Groups ON Team.GroupID = Groups.ID ORDER BY Groups.ID ASC");

        ResultSet allTeams = qAllTeams.executeQuery();

        while (allTeams.next()) {
            teams.add(
                    new Team(
                    allTeams.getInt("ID"),
                    allTeams.getString("School"),
                    allTeams.getString("TeamCaptain"),
                    allTeams.getString("Email"),
                    allTeams.getInt("GroupID"),
                    allTeams.getInt("Points"),
                    allTeams.getNString("GroupName")));
        }

        con.close();
        return teams;

    }
    @Override
    public ArrayList<Team> getAll() throws SQLException {
        Connection con = dS.getConnection();
        ArrayList<Team> teams = new ArrayList<>();

        PreparedStatement qAllTeams = con.prepareStatement("SELECT * FROM Team");

        ResultSet allTeams = qAllTeams.executeQuery();

        while (allTeams.next()) {
            teams.add(
                    new Team(
                    allTeams.getInt("ID"),
                    allTeams.getString("School"),
                    allTeams.getString("TeamCaptain"),
                    allTeams.getString("Email"),
                    allTeams.getInt("GroupID"),
                    allTeams.getInt("Points")));
                    
        }

        con.close();
        return teams;

    }
    /**
     * Selects a specific team from the database where the ID is equal to the parameter
     * 
     * @param id is used to find a specific. 
     * 
     * @return an instance of Team
     * @throws SQLException 
     */
    public Team getById(int id) throws SQLException {
        Connection con = dS.getConnection();
        PreparedStatement qTeam = con.prepareStatement("SELECT Team.*, Groups.GroupName FROM Team LEFT JOIN Groups ON Team.GroupID = Groups.ID WHERE Team.ID = ?");
        qTeam.setInt(1, id);
        ResultSet team = qTeam.executeQuery();
        team.next();
        
        Team newTeam = new Team(team.getInt("ID"),
                team.getString("School"),
                team.getString("TeamCaptain"),
                team.getString("Email"),
                team.getInt("GroupID"),
                team.getInt("Points"),
                team.getString("GroupName"));
        
        con.close();
        
        return newTeam;
        
       
    }
    /**
     * Creates the relation between a team and a group within the database. 
     * @param team
     * @throws SQLException 
     */
    public void assignToGroup(Team team, int groupId) throws SQLException {
        Connection con = dS.getConnection();

        PreparedStatement qTeam = con.prepareStatement("UPDATE Team SET GroupID = ? WHERE ID = ?");

        qTeam.setInt(1, groupId);
        qTeam.setInt(2, team.getID());
        qTeam.executeUpdate();

        con.close();
    }
    /**
     * Assigns points to the specific team into the Points column within the Team table of the database
     * @param team
     * @throws SQLException 
     */
    public void assignPoints(Team team) throws SQLException {
       Connection con = dS.getConnection();

        PreparedStatement qTeam = con.prepareStatement("UPDATE Team SET Points = ? WHERE ID = ?");

        qTeam.setInt(1, team.getPoints());
        qTeam.setInt(2, team.getID());

        qTeam.executeUpdate();

        con.close(); 
    }
    /**
     * Selects all teams within the database where the groupId matches the paramater. 
     * @param groupID is the ID that is compared to teams. 
     * @return all teams within a specific group
     * @throws SQLException 
     */
    public ArrayList<Team> getTeamsByGroup(Group group) throws SQLException {
        Connection con = dS.getConnection();
        ArrayList<Team> teams = new ArrayList<>();

        PreparedStatement qAllTeams = con.prepareStatement("SELECT Team.*, Groups.GroupName FROM Team INNER JOIN Groups ON Team.GroupID = Groups.ID WHERE Team.GroupID = ?");
        qAllTeams.setInt(1, group.getID());

        ResultSet allTeams = qAllTeams.executeQuery();

        while (allTeams.next()) {
            teams.add(
                    new Team(
                    allTeams.getInt("ID"),
                    allTeams.getString("School"),
                    allTeams.getString("TeamCaptain"),
                    allTeams.getString("Email"),
                    allTeams.getInt("GroupID"),
                    allTeams.getInt("Points"),
                    allTeams.getString("GroupName")));
        }

        con.close();
        return teams;
    }

    @Override
    public void removeAll() throws SQLException {
        Connection con = dS.getConnection();
        PreparedStatement qData = con.prepareStatement("DELETE FROM Team; DBCC CHECKIDENT (Team, RESEED, 0)");
        qData.executeUpdate();
                
        con.close();
    }
    
    public ArrayList<Team> getTeamByPoints(Group group) throws SQLException {
        Connection con = dS.getConnection();
        ArrayList<Team> teams = new ArrayList<>();

        PreparedStatement qAllTeams = con.prepareStatement("SELECT Team.*, Groups.GroupName FROM Team INNER JOIN Groups ON Team.GroupID = Groups.ID WHERE Team.GroupID = ? ORDER BY Team.Points DESC");
        qAllTeams.setInt(1, group.getID());

        ResultSet allTeams = qAllTeams.executeQuery();

        while (allTeams.next()) {
            teams.add(
                    new Team(
                    allTeams.getInt("ID"),
                    allTeams.getString("School"),
                    allTeams.getString("TeamCaptain"),
                    allTeams.getString("Email"),
                    allTeams.getInt("GroupID"),
                    allTeams.getInt("Points"),
                    allTeams.getString("GroupName")));
        }

        con.close();
        return teams;
    }
}

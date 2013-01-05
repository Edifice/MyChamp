package DAL;

import BE.Team;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TeamDBManager extends DBManager {

    public TeamDBManager() throws SQLException {
        super();
    }

    public void addTeam(Team team) throws SQLException {
        Connection con = dS.getConnection();
        PreparedStatement qTeam = con.prepareStatement("INSERT INTO Team VALUES (?, ?, ?, ?, ?)");

        qTeam.setInt(1, team.getID());
        qTeam.setString(2, team.getSchool());
        qTeam.setString(3, team.getTeamCaptain());
        qTeam.setString(4, team.getEmail());
        qTeam.setInt(5, team.getGroupID());

        qTeam.executeUpdate();

        con.close();
    }

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

    public void removeTeam(int id) throws SQLException {
        Connection con = dS.getConnection();
        PreparedStatement qTeam = con.prepareStatement("DELETE FROM Team WHERE ID = ?");
        qTeam.setInt(1, id);

        qTeam.executeUpdate();

        con.close();
    }

    ;
    
    public ArrayList<Team> getAllTeams() throws SQLException {
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
                    allTeams.getInt("GroupID")));
        }

        con.close();
        return teams;

    }

    public Team getTeamById(int id) throws SQLException {
        Connection con = dS.getConnection();
        PreparedStatement qTeam = con.prepareStatement("SELECT * FROM Team WHERE ID = ?");
        ResultSet team = qTeam.executeQuery();

        return new Team(team.getInt("ID"),
                team.getString("School"),
                team.getString("TeamCaptain"),
                team.getString("Email"),
                team.getInt("GroupID"));
    }
}
